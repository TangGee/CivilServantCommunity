package com.mdove.dependent.common.view.richspan

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlin.math.max

/**
 * Created by MDove on 2019-11-2.
 */
class PureRichContent(
    @SerializedName("type")
    val type: Int,
    @SerializedName("start")
    val start: Int,
    @SerializedName("length")
    val length: Int,
    @SerializedName("topic_id")
    var topicId: Long? = null,
    @SerializedName("mention_user_id")
    val mention_user_id: Long? = null
) {
    override fun toString(): String {
        return "PureRichContent(type=$type, start=$start, length=$length, topicId=$topicId,mention_user_id=$mention_user_id)"
    }
}

fun TitleRichContent.toPureBean(): PureRichContent {
    return PureRichContent(type, Math.max(start, 0), length, topicId, mention_user_id)
}

fun RichSpan.RichSpanItem.toPureBean(): PureRichContent {
    return PureRichContent(type, Math.max(start, 0), length, forumId, mentionUserId)
}

class BzImageForShort(
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("uri")
    val uri: String = "",
    @SerializedName("height")
    val height: Int = 0
)

@Parcelize
data class TitleRichContent(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: Int,
    @SerializedName("start")
    var start: Int,
    @SerializedName("length")
    var length: Int,
    @SerializedName("topic_id")
    var topicId: Long? = null,
    @SerializedName("mention_user_id")
    val mention_user_id: Long? = 0
) : Parcelable {

    val end: Int
        get() = start + length - 1

    @IgnoredOnParcel
    var ugcStart = -1   //ugc start index for Link Span
    @IgnoredOnParcel
    val isTopic: Boolean = type == TYPE_TOPIC
    @IgnoredOnParcel
    val isUserMention: Boolean = type == TYPE_MENTION
    @IgnoredOnParcel
    val isLink: Boolean = type == TYPE_LINK

    companion object {
        const val TYPE_TOPIC_APPEND = RichSpan.RichSpanItem.TYPE_TOPIC_APPEND
        const val TYPE_MENTION = RichSpan.RichSpanItem.TYPE_MENTION
        const val TYPE_TOPIC = RichSpan.RichSpanItem.TYPE_TOPIC
        const val TYPE_LINK = RichSpan.RichSpanItem.TYPE_LINK
        const val TYPE_LIVE_ROOM = RichSpan.RichSpanItem.TYPE_LIVE_ROOM

        const val LINK_STR = "Link"

        fun buildUserMentionRichContent(
            name: String? = null,
            start: Int,
            length: Int,
            userId: Long
        ): TitleRichContent =
            TitleRichContent(
                name,
                TYPE_MENTION,
                max(0, start),
                max(0, length),
                mention_user_id = userId
            )

        fun buildTopicRichContent(
            name: String? = null,
            start: Int,
            length: Int,
            forumId: Long?
        ): TitleRichContent =
            TitleRichContent(name, TYPE_TOPIC, max(0, start), max(0, length), topicId = forumId)
    }
}
