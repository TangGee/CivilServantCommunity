package com.mdove.dependent.common.view.richspan

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mdove.dependent.common.R
import com.mdove.dependent.common.log.Logger
import com.mdove.dependent.common.view.richspan.EditTextForUGC.Companion.UGC_LINK_STRING
import kotlinx.android.parcel.Parcelize
import kotlin.math.max

/**
 * Created by MDove on 2019-11-2.
 */
sealed class UgcPostEditItem {
    abstract fun isEdited(): Boolean
}

@Parcelize
data class RichSpan @JvmOverloads constructor(
    @SerializedName("links")
    val links: MutableList<RichSpanItem>? = null
) : Parcelable {

    @Parcelize
    data class RichSpanItem(
        @SerializedName("link") val link: String = "",
        @SerializedName("start") var start: Int = 0,
        @SerializedName("length") var length: Int = 0,
        @SerializedName("type") val type: Int = 0,
        @SerializedName("mention_user_id") val mentionUserId: Long? = null,
        @SerializedName("forum_id") var forumId: Long? = null
    ) : Parcelable {

        val isMention: Boolean
            get() = type == 1 && mentionUserId != 0L

        companion object {
            @Deprecated("for compatibility")
            const val TYPE_TOPIC_APPEND = 0
            const val TYPE_MENTION = 1
            const val TYPE_TOPIC = 2
            const val TYPE_LINK = 3
            const val TYPE_LIVE_ROOM = 4
        }
    }

    companion object {
        @JvmStatic
        fun validateRichSpan(richSpan: RichSpan?): Boolean {
            if (richSpan?.links == null || richSpan.links.isEmpty()) {
                return false
            }
            return true
        }
    }
}

data class TopicBean(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Long
)

data class UgcPostEditTitleEditItem constructor(
    var title: String,
    var richSpanList: MutableList<TitleRichContent>,
    val titleMaxLength: Int,
    val isRepost: Boolean,
    val hintString: String,
    var selection: Int = title.length,
    var traceId: String = "null in UgcPostEditTitleEditItem"
) : UgcPostEditItem() {

    private val _selection: Int
        get() {
            // update selection
            if (selection > title.length) {
                Logger.e("mdove", "Incorrect selection $selection , current title is $title")
                selection = title.length
            }
            return selection
        }

    private var _title: String = title

    private var _richSpanList = mutableListOf<TitleRichContent>().apply {
        richSpanList.forEach {
            add(it.copy())
        }
    }

    val ugcLength: Int
        get() {
            var result = title.length
            richSpanList.forEach {
                if (it.isLink) {
                    result = result - it.length + UGC_LINK_STRING.length
                }
            }
            return result
        }

    fun removeRichSpan(hitTarget: (TitleRichContent) -> Boolean): Boolean {
        val iter = richSpanList.iterator()
        while (iter.hasNext()) {
            val cursorBean = iter.next()
            if (hitTarget(cursorBean)) {
                // https://fabric.io/bytedance18/android/apps/app.buzz.share/issues/5cdcfdc1f8b88c29630bb59b?time=last-seven-days
                return try {
                    iter.remove()
                    title = title.removeRange(
                        cursorBean.start,
                        cursorBean.start + cursorBean.length
                    )
                    updateRichSpanAnchor(
                        cursorBean.start,
                        -cursorBean.length
                    )
                    selection = max(selection - cursorBean.length, 0)
                    true
                } catch (e: Exception) {
                    Logger.e("mdove", e.toString())
                    false
                }
            }
        }
        return false
    }

    fun updateRichSpanAnchor(start: Int, offset: Int) {
        richSpanList.forEach {
            if (it.start < 0 || it.start > start) {
                it.start += offset
            }
        }
    }

//    fun addMention(name: String, userID: Long, fromBar: Boolean) {
//        if (fromBar) {
//            val appendString = "@$name "
//            updateRichSpanAnchor(_selection - 1, appendString.length)
//            richSpanList.add(
//                TitleRichContent.buildUserMentionRichContent(
//                    name,
//                    _selection,
//                    appendString.length,
//                    userID
//                )
//            )
//            when (_selection) {
//                title.length -> title += appendString
//                0 -> title = appendString + title
//                else -> title =
//                    title.substring(0, _selection) + appendString + title.substring(_selection)
//            }
//            selection += appendString.length
//        } else {
//            addRichContent(name, '@') { realName, startPos ->
//                TitleRichContent.buildUserMentionRichContent(
//                    realName,
//                    startPos,
//                    realName.length,
//                    userID
//                )
//            }
//        }
//    }

    private fun addRichContent(
        name: String,
        prefixChar: Char,
        mentionRichContent: (String, Int) -> TitleRichContent
    ) {
        val realName = "$prefixChar$name "
        val triggerIndex = _selection - 1
        val startPos = title.lastIndexOf(prefixChar, Math.max(0, triggerIndex))
        if (startPos in 0..triggerIndex) {
            // hit
            val offset = triggerIndex - startPos + 1
            updateRichSpanAnchor(startPos, realName.length - offset)

            title.let { originTitle ->
                title = originTitle.substring(
                    0,
                    startPos
                ) + realName + if (triggerIndex < originTitle.length - 1) originTitle.substring(
                    triggerIndex + 1
                ) else ""
            }

            richSpanList.add(
                mentionRichContent(realName, startPos)
            )

            selection = startPos + realName.length
        }
    }

    fun addTopics(topic: TopicBean, fromBar: Boolean, initial: Boolean = false) {
        if (fromBar) {
            val appendString = "#" + topic.name + " "
            updateRichSpanAnchor(_selection - 1, appendString.length)
            richSpanList.add(
                TitleRichContent.buildTopicRichContent(
                    topic.name,
                    _selection,
                    appendString.length,
                    topic.id
                )
            )
            when {
                _selection >= title.length -> title += appendString
                _selection == 0 -> title = appendString + title
                else -> {
                    Logger.e("asdf", "====asdf $title $_selection")
                    title =
                        title.substring(0, _selection) + appendString + title.substring(_selection)
                }
            }
            selection += appendString.length
        } else {
            addRichContent(topic.name, '#') { realName, startPos ->
                TitleRichContent.buildTopicRichContent(
                    realName,
                    startPos,
                    topic.name.length + 2,
                    topic.id
                )
            }
        }
        if (initial) {
            _title = title
            _richSpanList.clear()
            richSpanList.forEach {
                _richSpanList.add(it.copy())
            }
        }
    }

    /**
     * 若新的Span与旧Span有冲突，以新的为准。
     */
    private fun removeConflictsLinkSpan(element: TitleRichContent): Boolean {
        val iter = richSpanList.iterator()
        while (iter.hasNext()) {
            val cursor = iter.next()
            if (cursor.isLink) {
                val spanRange = IntRange(cursor.start, cursor.end)
                if (element.start in spanRange || element.end in spanRange) {
                    iter.remove()
                }
            }
        }
        return true
    }

    override fun isEdited(): Boolean {
        if (_title != title) {
            return true
        }
        return false
    }
}