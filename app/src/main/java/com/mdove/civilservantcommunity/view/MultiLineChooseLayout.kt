package com.mdove.civilservantcommunity.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.mdove.civilservantcommunity.R
import java.util.ArrayList

/**
 * Created by MDove on 2019-09-02.
 */
class MultiLineChooseLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MultiLineChooseLayoutTagsStyle
) : ViewGroup(context, attrs, defStyleAttr) {

    /**
     * 默认的文字颜色
     */
    private val default_text_color = Color.rgb(0x00, 0xA8, 0xFF)

    /**
     * 默认的背景颜色
     */
    private val default_background_color = Color.WHITE

    /**
     * 默认的选中文字颜色
     */
    private val default_checked_text_color = Color.WHITE

    /**
     * 默认的选中背景颜色
     */
    private val default_checked_background_color = Color.rgb(0x00, 0xA8, 0xFF)

    /**
     * 默认的文字大小
     */
    private val default_text_size: Float

    /**
     * 默认的水平间距
     */
    private val default_horizontal_spacing: Float

    /**
     * 默认的竖直间距
     */
    private val default_vertical_spacing: Float

    /**
     * 默认的内部水平间距
     */
    private val default_horizontal_padding: Float

    /**
     * 默认的内部竖直间距
     */
    private val default_vertical_padding: Float

    private var textColor: Int = 0

    private var mLayoutBackgroundColor: Int = 0

    private var selectedTextColor: Int = 0

    private var selectedBackgroundColor: Int = 0

    private var textSize: Float = 0.toFloat()

    private var horizontalSpacing: Int = 0

    private var verticalSpacing: Int = 0

    private var horizontalPadding: Int = 0

    private var verticalPadding: Int = 0

    private var itemWidth: Int = 0
    private var itemHeight: Int = 0

    private var itemMaxEms: Int = 0

    private var multiChooseable: Boolean = false
    private var singleLine: Boolean = false

    private var animUpdateDrawable = false

    //textview的属性
    private val mRadius = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    private var mStrokeColor: ColorStateList? = null
    private var mCheckedStrokeColor: ColorStateList? = null

    private var mStrokeWidth = 0

    private var mOnItemClickLisener: onItemClickListener? = null

    private val mInternalTagClickListener = ItemClicker()

    /**
     * Returns the tag array in group, except the INPUT tag.
     *
     * @return the tag array.
     */
    val allItemText: List<String>
        get() {
            val count = childCount
            val tagList = ArrayList<String>()
            for (i in 0 until count) {
                val tagView = getIndexItem(i)
                tagList.add(tagView!!.text.toString())
            }

            return tagList
        }

    /**
     * 返回选中的item
     * @return
     */
    protected val selectedItem: ItemView?
        get() {
            val checkedTagIndex = selectedIndex
            return if (checkedTagIndex != -1) {
                getIndexItem(checkedTagIndex)
            } else null
        }

    /**
     * 获取选中的item文字内容
     * @return
     */
    protected val selectedItemText: String?
        get() = if (null != selectedItem) {
            selectedItem!!.text.toString()
        } else null

    /**
     * 返回所有选中的tag的文字
     * @return String
     */
    val allItemSelectedTextWithStringArray: Array<String>
        get() {
            val count = childCount
            val tagList = ArrayList<String>()
            for (i in 0 until count) {
                getIndexItem(i)?.let {
                    if (it.isChecked) {
                        tagList.add(it.text.toString())
                    }
                }
            }

            return tagList.toTypedArray()
        }

    /**
     * 返回所有选中的tag的文字
     * @return ListArray
     */
    val allItemSelectedTextWithListArray: ArrayList<String>
        get() {
            val count = childCount
            val tagList = ArrayList<String>()
            for (i in 0 until count) {
                val tagView = getIndexItem(i)
                if (tagView!!.isChecked) {
                    tagList.add(tagView.text.toString())
                }
            }

            return tagList
        }

    /**
     * 返回选中的item下标
     * @return
     */
    val selectedIndex: Int
        get() {
            val count = childCount
            for (i in 0 until count) {
                val tag = getIndexItem(i)
                if (tag!!.isChecked) {
                    return i
                }
            }
            return -1
        }

    /**
     * 返回所有选中的item的下标列表集合
     * @return ArrayList
     */
    val allItemSelectedIndex: ArrayList<Int>
        get() {
            val count = childCount
            val tagList = ArrayList<Int>()
            for (i in 0 until count) {
                val tagView = getIndexItem(i)
                if (tagView!!.isChecked) {
                    tagList.add(i)
                }
            }

            return tagList
        }

    init {
        default_text_size = sp2px(13.0f).toFloat()
        default_horizontal_spacing = dp2px(8.0f)
        default_vertical_spacing = dp2px(4.0f)
        default_horizontal_padding = dp2px(0.0f)
        default_vertical_padding = dp2px(0.0f)

        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MultiLineChooseItemTags,
            defStyleAttr,
            R.style.MultiLineChooseItemTags
        )
        try {
            textColor = attrsArray.getColor(
                R.styleable.MultiLineChooseItemTags_item_textColor,
                default_text_color
            )
            mLayoutBackgroundColor = attrsArray.getColor(
                R.styleable.MultiLineChooseItemTags_item_backgroundColor,
                default_background_color
            )
            selectedTextColor = attrsArray.getColor(
                R.styleable.MultiLineChooseItemTags_item_selectedTextColor,
                default_checked_text_color
            )
            selectedBackgroundColor = attrsArray.getColor(
                R.styleable.MultiLineChooseItemTags_item_selectedBackgroundColor,
                default_checked_background_color
            )
            textSize = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_textSize,
                default_text_size
            )
            horizontalSpacing = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_horizontalSpacing,
                default_horizontal_spacing
            ).toInt()
            verticalSpacing = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_verticalSpacing,
                default_vertical_spacing
            ).toInt()
            horizontalPadding = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_horizontalPadding,
                default_horizontal_padding
            ).toInt()
            verticalPadding = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_verticalPadding,
                default_vertical_padding
            ).toInt()
            multiChooseable = attrsArray.getBoolean(
                R.styleable.MultiLineChooseItemTags_item_multiChooseable,
                true
            )
            singleLine =
                attrsArray.getBoolean(R.styleable.MultiLineChooseItemTags_item_singleLine, false)
            itemWidth = attrsArray.getInt(
                R.styleable.MultiLineChooseItemTags_item_width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (itemWidth >= 0) {
                itemWidth = sp2px(itemWidth.toFloat())
            }
            itemHeight = attrsArray.getInt(
                R.styleable.MultiLineChooseItemTags_item_height,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (itemHeight >= 0) {
                itemHeight = sp2px(itemHeight.toFloat())
            }

            itemMaxEms = attrsArray.getInt(R.styleable.MultiLineChooseItemTags_item_maxEms, -1)
            if (itemWidth < 0) {
                itemMaxEms = -1
            }

            val radius =
                attrsArray.getDimension(R.styleable.MultiLineChooseItemTags_item_radius, 0f)
            var topLeftRadius =
                attrsArray.getDimension(R.styleable.MultiLineChooseItemTags_item_topLeftRadius, 0f)
            var topRightRadius =
                attrsArray.getDimension(R.styleable.MultiLineChooseItemTags_item_topRightRadius, 0f)
            var bottomLeftRadius = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_bottomLeftRadius,
                0f
            )
            var bottomRightRadius = attrsArray
                .getDimension(R.styleable.MultiLineChooseItemTags_item_bottomRightRadius, 0f)

            if (topLeftRadius == 0f && topRightRadius == 0f && bottomLeftRadius == 0f && bottomRightRadius == 0f) {
                bottomLeftRadius = radius
                bottomRightRadius = bottomLeftRadius
                topRightRadius = bottomRightRadius
                topLeftRadius = topRightRadius
            }

            mRadius[1] = topLeftRadius
            mRadius[0] = mRadius[1]
            mRadius[3] = topRightRadius
            mRadius[2] = mRadius[3]
            mRadius[5] = bottomRightRadius
            mRadius[4] = mRadius[5]
            mRadius[7] = bottomLeftRadius
            mRadius[6] = mRadius[7]

            mStrokeColor =
                attrsArray.getColorStateList(R.styleable.MultiLineChooseItemTags_item_strokeColor)
            mCheckedStrokeColor = attrsArray
                .getColorStateList(R.styleable.MultiLineChooseItemTags_item_selectedStrokeColor)
            mStrokeWidth = attrsArray.getDimension(
                R.styleable.MultiLineChooseItemTags_item_strokeWidth,
                mStrokeWidth.toFloat()
            ).toInt()

        } finally {
            attrsArray.recycle()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var width = 0
        var height = 0

        var row = 0 // The row counter.
        var rowWidth = 0 // Calc the current row width.
        var rowMaxHeight = 0 // Calc the max tag height, in current row.

        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            if (child.visibility != View.GONE) {
                rowWidth += childWidth
                if (rowWidth > widthSize) { // Next line.
                    rowWidth = childWidth // The next row width.
                    height += rowMaxHeight + verticalSpacing
                    rowMaxHeight = childHeight // The next row max height.
                    row++
                } else { // This line.
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight)
                }
                rowWidth += horizontalSpacing
            }
        }
        height += rowMaxHeight

        height += paddingTop + paddingBottom

        if (row == 0) {//只有一行item
            width = rowWidth
            width += paddingLeft + paddingRight
        } else {// If the tags grouped exceed one line, set the width to match the parent.
            width = widthSize
        }

        setMeasuredDimension(
            if (widthMode == View.MeasureSpec.EXACTLY) widthSize else width,
            if (heightMode == View.MeasureSpec.EXACTLY) heightSize else height
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentLeft = paddingLeft
        val parentRight = r - l - paddingRight
        val parentTop = paddingTop
        val parentBottom = b - t - paddingBottom

        var childLeft = parentLeft
        var childTop = parentTop

        var rowMaxHeight = 0

        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val width = child.measuredWidth
            val height = child.measuredHeight

            if (child.visibility != View.GONE) {
                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft
                    childTop += rowMaxHeight + verticalSpacing
                    rowMaxHeight = height
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height)
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height)

                childLeft += width + horizontalSpacing
            }
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.tags = allItemText
        ss.checkedPosition = selectedIndex
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        state.tags?.let {
            setList(it)
        }
        val checkedTagView = getIndexItem(state.checkedPosition)
        checkedTagView?.setItemSelected(true)
    }

    /**
     * 设置数据源
     * @param tags
     */
    fun setList(tagList: List<String>) {
        removeAllViews()
        for (tag in tagList) {
            addItem(tag)
        }
    }

    /**
     * 设置默认的位置上选中
     * @param position
     * @return
     */
    fun setIndexItemSelected(position: Int): Int {

        var index = -1
        val count = childCount
        if (position >= count) {

            return -1
        }

        val tagView = getIndexItem(position)
        tagView!!.setItemSelected(true)
        index = position
        return index
    }

    /**
     * 返回指定的item
     * @param index
     * @return
     */
    protected fun getIndexItem(index: Int): ItemView? {
        return if (null == getChildAt(index)) null else getChildAt(index) as ItemView
    }

    /**
     * 取消选中状态
     */
    fun cancelAllSelectedItems() {
        val count = childCount
        for (i in 0 until count) {
            val tag = getIndexItem(i)
            if (null != tag && tag.isChecked) {
                tag.setItemSelected(false)
            }
        }
    }

    /**
     * 增加item
     * @param tag
     */
    private fun addItem(tag: CharSequence) {
        val item = ItemView(context, tag)
        item.setOnClickListener(mInternalTagClickListener)
        addView(item)
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
            .toInt()
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(context, attrs)
    }

    fun setOnItemClickListener(l: onItemClickListener) {
        mOnItemClickLisener = l
    }

    interface onItemClickListener {
        fun onItemClick(position: Int, text: String)
    }

    /**
     * For [MultiLineChooseLayout] save and restore state.
     */
    inner class SavedState : View.BaseSavedState {

        var tagCount: Int = 0

        var tags: List<String>? = null

        var checkedPosition: Int = 0

        constructor(source: Parcel) : super(source) {
            tagCount = source.readInt()
            source.readStringList(tags)
            checkedPosition = source.readInt()
        }

        constructor(superState: Parcelable) : super(superState) {}

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            tagCount = tags?.size ?: 0
            dest.writeInt(tagCount)
            dest.writeStringList(tags)
            dest.writeInt(checkedPosition)
        }
    }

    internal inner class ItemClicker : View.OnClickListener {
        override fun onClick(v: View) {
            val tag = v as ItemView
            var position = -1
            val checkedTag = selectedItem
            if (!multiChooseable) {
                //单选
                checkedTag?.setItemSelected(false)

                tag.setItemSelected(true)
                position = selectedIndex
            } else {
                //多选
                tag.setItemSelected(!tag.isChecked)
                position = -1
            }

            //外部点击事件
            if (mOnItemClickLisener != null) {
                mOnItemClickLisener!!.onItemClick(position, tag.text.toString())
            }

        }
    }

    inner class ItemView(private val mContext: Context, text: CharSequence) :
        AppCompatTextView(mContext) {

        var isChecked = false

        private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        private val mOutRect = Rect()

        init {
            mBackgroundPaint.style = Paint.Style.FILL
        }

        init {
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
            layoutParams = ViewGroup.LayoutParams(itemWidth, itemHeight)

            gravity = Gravity.CENTER

            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

            setSingleLine(singleLine)
            if (singleLine) {
                if (itemMaxEms >= 0) {
                    ellipsize = TextUtils.TruncateAt.valueOf("END")
                    maxEms = itemMaxEms
                }
            }

            setText(text)

            isClickable = true
            invalidatePaint()

        }

        /**
         * Set whether this tag view is in the checked state.
         *
         * @param select true is checked, false otherwise
         */
        fun setItemSelected(select: Boolean) {
            isChecked = select
            invalidatePaint()
        }

        override fun getDefaultEditable(): Boolean {
            return false
        }

        private fun invalidatePaint() {
            animUpdateDrawable = false

            if (isChecked) {
                mBackgroundPaint.color = selectedBackgroundColor
                setTextColor(selectedTextColor)
            } else {
                mBackgroundPaint.color = mLayoutBackgroundColor
                setTextColor(textColor)
            }

        }

        override fun onDraw(canvas: Canvas) {
            if (!animUpdateDrawable) {
                updateDrawable()
            }
            super.onDraw(canvas)
        }

        private fun updateDrawable() {
            mStrokeColor =
                if (mStrokeColor == null) ColorStateList.valueOf(Color.TRANSPARENT) else mStrokeColor
            mCheckedStrokeColor =
                if (mCheckedStrokeColor == null) mStrokeColor else mCheckedStrokeColor
            updateDrawable(if (!isChecked) mStrokeColor!!.defaultColor else mCheckedStrokeColor!!.defaultColor)
        }

        private fun updateDrawable(strokeColor: Int) {

            val mbackgroundColor: Int
            if (isChecked) {
                mbackgroundColor = selectedBackgroundColor
            } else {
                mbackgroundColor = mLayoutBackgroundColor
            }

            val drawable = GradientDrawable()
            drawable.cornerRadii = mRadius
            drawable.setColor(mbackgroundColor)
            drawable.setStroke(mStrokeWidth, strokeColor)

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                this.setBackgroundDrawable(drawable)
            } else {
                this.background = drawable
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    getDrawingRect(mOutRect)
                    invalidatePaint()
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!mOutRect.contains(event.x.toInt(), event.y.toInt())) {
                        invalidatePaint()
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    invalidatePaint()
                    invalidate()
                }
            }
            return super.onTouchEvent(event)
        }
    }
}
