package com.mdove.dependent.common.view.richspan

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.mdove.dependent.common.R
import com.mdove.dependent.common.log.Logger

/**
 * Created by MDove on 2019-11-2.
 */
class EditTextForUGC @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ET_U"
        const val UGC_LINK_STRING = "Link"
    }

    private var textWatcherList: MutableList<TextWatcher> = mutableListOf()

    private var mentionTextColor: Int

    private val linkIcon: Drawable

    private var underline: Boolean

    var selectionChangeListener: ((Int, Int) -> Unit)? = null

    private var _selectionChangeListener: ((Int, Int) -> Unit)? = null

    init {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EditTextForUGC)
        mentionTextColor =
            typedArray.getColor(R.styleable.EditTextForUGC_highlightColor, Color.BLUE)
        underline =
            typedArray.getBoolean(R.styleable.EditTextForUGC_useUnderline, false)

        linkIcon = DrawableCompat.wrap(
            VectorDrawableCompat.create(
                resources,
                R.drawable.vector_ic_link,
                null
            )!!
        )
        DrawableCompat.setTintList(linkIcon, ColorStateList.valueOf(mentionTextColor))
        updateLinkIconSize()
        typedArray.recycle()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selfChange || !resetSelection(selStart, selEnd)) {
            selfChange = false
            selectionChangeListener?.invoke(selStart, selEnd)
        }
    }

    private fun getTextSpan(): Array<ForegroundColorSpan> {
        return text?.let {
            return it.getSpans(0, it.length, ForegroundColorSpan::class.java)
        } ?: emptyArray()
    }

    private var selfChange:Boolean = false

    private fun resetSelection(start: Int, end: Int): Boolean {
        var selStart = start
        var selEnd = end

        text?.let { editable ->
            val richSpansPairs = mutableListOf<Pair<Int, Int>>().apply {
                addAll(getLinkSpan().map {
                    Pair(editable.getSpanStart(it), editable.getSpanEnd(it) + 1)
                })
                addAll(getTextSpan().map {
                    Pair(editable.getSpanStart(it), editable.getSpanEnd(it))
                })
            }
            var result = false
            for (i in richSpansPairs.indices) {
                if (selStart > richSpansPairs[i].first && selStart < richSpansPairs[i].second) {
                    result = true
                    selStart =
                        if (selStart - richSpansPairs[i].first > richSpansPairs[i].second - selStart
                        ) {
                            richSpansPairs[i].second
                        } else {
                            richSpansPairs[i].first
                        }
                    break
                }
                if (selEnd > richSpansPairs[i].first && selEnd < richSpansPairs[i].second) {
                    result = true
                    selEnd =
                        if (selEnd - richSpansPairs[i].first > richSpansPairs[i].second - selStart
                        ) {
                            richSpansPairs[i].second
                        } else {
                            richSpansPairs[i].first
                        }
                    break
                }
            }
            if (result) {
                val textLength = text?.length ?: 0
                val targetStart = Math.min(Math.max(0, selStart), textLength)
                val targetEnd = Math.min(selEnd, textLength)
                if (targetStart != start || targetEnd != end) {
                    try {
                        selfChange = true
                        setSelection(
                            targetStart,
                            targetEnd
                        )
                    } catch (e: Exception) {
                        Logger.e(TAG, e.toString())
                    }
                    return true
                }
            }
        }
        return false
    }


    private fun updateLinkIconSize() {
        val height = (textSize * 0.75).toInt()
        val width = height * linkIcon.intrinsicWidth / linkIcon.intrinsicHeight
        linkIcon.setBounds(0, 0, width, height)
    }

    override fun addTextChangedListener(watcher: TextWatcher?) {
        super.addTextChangedListener(watcher)

        watcher?.let {
            textWatcherList.add(watcher)
        }
    }

    override fun removeTextChangedListener(watcher: TextWatcher?) {
        super.removeTextChangedListener(watcher)

        watcher?.let {
            textWatcherList.remove(watcher)
        }
    }

    override fun setSelection(index: Int) {
        super.setSelection(Math.min(index, text?.length ?: 0))
    }

    /**
     * do without customize text watcher & selection change listener
     */
    private fun steal(steal: (EditTextForUGC) -> Unit) {
        textWatcherList.forEach {
            super.removeTextChangedListener(it)
        }
        _selectionChangeListener = selectionChangeListener
        selectionChangeListener = null
        steal(this)
        textWatcherList.forEach {
            super.addTextChangedListener(it)
        }
        selectionChangeListener = _selectionChangeListener
        _selectionChangeListener = null
    }

    fun makeTextSpan(start: Int, end: Int): Boolean {
        val span = ForegroundColorSpan(mentionTextColor)
        return if (start != -1 && end != -1) {
            try {
                text?.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (underline) {
                    text?.setSpan(UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                true
            } catch (e: Exception) {
                Logger.e(TAG, e.toString())
                false
            }
        } else {
            false
        }
    }

    private fun makeLinkSpan(start: Int, realText: String, bgColor: Int? = null): Boolean {
        Logger.d("MDove", "makeLinkSpan $start $realText")
        val span = CustomImageSpan(linkIcon, CustomImageSpan.ALIGN_BASELINE, false)
        bgColor?.let { span.setSpanBgColor(it) }
        span.setTextColor(mentionTextColor)
        val end = start + realText.trim().length
        span.mText = UGC_LINK_STRING
        span.mRealString = realText.trim() + " "

        return if (start != -1 && end != -1) {
            try {
                text?.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                true
            } catch (e: Exception) {
                Logger.e(TAG, e.toString())
                false
            }
        } else {
            false
        }
    }

    private fun makeTextBgColorSpan(start: Int, end: Int, color: Int): Boolean {
        val span = BackgroundColorSpan(color)
        return if(start != -1 && end != -1) {
            try {
                text?.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                true
            } catch (e: Exception) {
                false
            }
        } else {
            false
        }
    }

    private fun clearBgColorSpans() {
        val bgColorSpans = text?.let {
            it.getSpans(0, it.length, BackgroundColorSpan::class.java)
        } ?: emptyArray()
        bgColorSpans.forEach {
            try {
                editableText.removeSpan(it)
            } catch (e: Exception) {
                Logger.e("mdove",e.toString())
            }
        }
    }

    private fun clearLinkSpans() {
        getLinkSpan().forEach {
            try {
                editableText.removeSpan(it)
            } catch (e: Exception) {
                Logger.e("mdove",e.toString())
            }
        }
    }

    private fun getLinkSpan(): Array<CustomImageSpan> {
        return text?.let {
            return it.getSpans(0, it.length, CustomImageSpan::class.java)
        } ?: emptyArray()
    }

    fun bindTitleItem(item: UgcPostEditTitleEditItem) {
        Logger.d("ZDITTEXT", " bindTitleItem is $item")
        hint = item.hintString
        if (item.title != this.text.toString()) {
            steal {
                setText(item.title)
            }
        }
        if (this.selectionEnd != item.selection) {
            setSelection(item.selection)
        }

        clearLinkSpans()
        clearBgColorSpans()
        computeLinkSpansUgcStart(item)

        if(item.ugcLength > item.titleMaxLength) {
            val str = item.title
            val beyondStartIndex = item.titleMaxLength + getLinkSpanDiffNumber(item)
            if(Character.isHighSurrogate(str[beyondStartIndex - 1]) && Character.isLowSurrogate(str[beyondStartIndex])) {
                makeTextBgColorSpan(beyondStartIndex - 1, item.title.length,
                    ContextCompat.getColor(context, R.color.red_200))
            } else {
                makeTextBgColorSpan(beyondStartIndex, item.title.length,
                    ContextCompat.getColor(context, R.color.red_200))
            }
        }

        item.richSpanList.forEach {
            if (it.isLink) {
                if(it.ugcStart + UGC_LINK_STRING.length <= item.titleMaxLength) {
                    makeLinkSpan(it.start, it.name ?: "")
                } else {
                    makeLinkSpan(it.start, it.name ?: "", ContextCompat.getColor(context, R.color.red_200))
                }

            } else if (it.isTopic || it.isUserMention) {
                makeTextSpan(it.start, it.start + it.length)
            }
        }
    }

    private fun computeLinkSpansUgcStart(item: UgcPostEditTitleEditItem) {
        val linkRichList = item.richSpanList.filter { it.isLink }.sortedBy { it.start }
        if(linkRichList.isNotEmpty()) {
            var diffCount = 0
            for(i in linkRichList.indices) {
                linkRichList[i].ugcStart = linkRichList[i].start - diffCount
                diffCount = diffCount + linkRichList[i].length - UGC_LINK_STRING.length
            }
        }
    }

    private fun getLinkSpanDiffNumber(item: UgcPostEditTitleEditItem) : Int {
        val linkRichList = item.richSpanList.filter { it.isLink }.filter { it.ugcStart + UGC_LINK_STRING.length <= item.titleMaxLength }
        var diffCount = 0
        for(richSpan in linkRichList) {
            diffCount += richSpan.length - UGC_LINK_STRING.length
        }
        return diffCount
    }
}
