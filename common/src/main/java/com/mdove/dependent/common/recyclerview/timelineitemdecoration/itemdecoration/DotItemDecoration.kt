package com.mdove.dependent.common.recyclerview.timelineitemdecoration.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View

import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.mdove.dependent.common.recyclerview.timelineitemdecoration.util.Util

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import android.R.attr.drawableRight
import com.mdove.dependent.common.R

/**
 * *          _       _
 * *   __   _(_)_   _(_) __ _ _ __
 * *   \ \ / / \ \ / / |/ _` | '_ \
 * *    \ V /| |\ V /| | (_| | | | |
 * *     \_/ |_| \_/ |_|\__,_|_| |_|
 *
 *
 * Created by vivian on 2017/6/9.
 *
 * copy from https://github.com/vivian8725118/TimeLine
 */

class DotItemDecoration(private val mContext: Context, private val mConfig: Config) :
    RecyclerView.ItemDecoration() {
    private val mLinePaint: Paint
    private var mDrawable: Drawable? = null
    private val mDotPaint: Paint
    private val mTextPaint: Paint
    private val mTextRect: Rect

    private var mSpanIndexListener: SpanIndexListener? = null

    //you can choose to draw or use resource as divider
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(STYLE_DRAW, STYLE_RESOURCE)
    annotation class ItemStyle

    //you can choose the orientation of item decoration
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(VERTICAL, HORIZONTAL)
    annotation class Orientation

    init {
        mTextPaint = Paint()
        mTextRect = Rect()
        mLinePaint = Paint()
        mDotPaint = Paint()
        mDotPaint.isAntiAlias = true
    }

    fun setSpanIndexListener(spanIndexListener: SpanIndexListener) {
        this.mSpanIndexListener = spanIndexListener
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter!!.itemCount
        val currentPosition = parent.getChildAdapterPosition(view)

        if (mConfig.mOrientation == VERTICAL) {
            outRect.left = mConfig.mItemPaddingLeft
            outRect.right = mConfig.mItemPaddingRight
            outRect.bottom = mConfig.mItemInterval
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mConfig.mTopDistance
            } else if (parent.getChildAdapterPosition(view) == 1) {
                outRect.top = 2 * mConfig.mTopDistance
            } else if (parent.adapter != null && (currentPosition == itemCount - 1 || currentPosition == itemCount - 2)) {
                outRect.bottom =
                    outRect.height() + mConfig.mItemInterval + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize + mTextRect.height() + mConfig.mDotRadius
            }
        } else {
            outRect.top = mConfig.mItemPaddingLeft
            outRect.bottom = mConfig.mItemPaddingRight
            outRect.right = mConfig.mItemInterval
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = mConfig.mTopDistance
            } else if (parent.getChildAdapterPosition(view) == 1) {
                outRect.left = 2 * mConfig.mTopDistance
            } else if (parent.adapter != null && (currentPosition == itemCount - 1 || currentPosition == itemCount - 2)) {
                outRect.right =
                    outRect.width() + mConfig.mItemInterval + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize + mTextRect.width() + mConfig.mDotRadius
            }
        }

        if (mSpanIndexListener == null) return

        val layoutParams = view.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val spanIndex = layoutParams.spanIndex
            if (mSpanIndexListener != null && spanIndex != -1) {
                mSpanIndexListener!!.onSpanIndexChange(view, spanIndex)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mConfig.mStyle == STYLE_RESOURCE) {
            mDrawable = ContextCompat.getDrawable(mContext, mConfig.mDotRes)
        }

        mTextPaint.color = mConfig.mTextColor
        mTextPaint.textSize = mConfig.mTextSize.toFloat()

        mLinePaint.color = mConfig.mLineColor
        mLinePaint.strokeWidth = mConfig.mLineWidth.toFloat()

        mDotPaint.color = mConfig.mDotColor

        if (mConfig.mOrientation == VERTICAL) {
            drawVerticalLine(c, parent)
            drawVerticalItem(c, parent)
        } else {
            drawHorizontalLine(c, parent)
            drawHorizontalItem(c, parent)
        }
    }

    private fun drawVerticalLine(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val parentWidth = parent.measuredWidth
        val bottom: Int

        val childCount = parent.childCount
        if (childCount == 0) return
        val lastChild = parent.getChildAt(childCount - 1)
        if (childCount > 1) {
            val child = parent.getChildAt(childCount - 2)
            bottom =
                mConfig.mBottomDistance + if (child.bottom > lastChild.bottom) child.bottom else lastChild.bottom
        } else {
            bottom = mConfig.mBottomDistance + lastChild.bottom
        }
        val lineX = if (mConfig.mOnlyLeft)
            mConfig.mOnlyLeftMarginLeft - mConfig.mLineWidth / 2 +mConfig.mDotRadius/2
        else
            (parentWidth / 2).toFloat()
        c.drawLine(
                lineX,
                top.toFloat(),
                lineX,
                bottom.toFloat(),
                mLinePaint
        )
    }

    private fun drawVerticalItem(c: Canvas, parent: RecyclerView) {
        val parentWidth = parent.measuredWidth
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            var top = child.top + mConfig.mDotPaddingTop
            var bottom: Int
            val drawableLeft: Int
            if (mConfig.mStyle == STYLE_RESOURCE) {
                bottom = top + mDrawable!!.intrinsicHeight
                drawableLeft = parentWidth / 2 - mDrawable!!.intrinsicWidth / 2
                val drawableRight = parentWidth / 2 + mDrawable!!.intrinsicWidth / 2

                mDrawable!!.setBounds(drawableLeft, top, drawableRight, bottom)
                mDrawable!!.draw(c)
            } else {
                drawableLeft = if (mConfig.mOnlyLeft) mConfig.mOnlyLeftMarginLeft.toInt() else parentWidth / 2

                if (mConfig.mDotInItemCenter) {
                    c.drawCircle(
                        drawableLeft.toFloat(),
                        ((child.top + child.bottom) / 2 + mConfig.mDotPaddingTop).toFloat(),
                        mConfig.mDotRadius.toFloat(),
                        mDotPaint
                    )
                } else {
                    c.drawCircle(
                        drawableLeft.toFloat(),
                        top.toFloat(),
                        mConfig.mDotRadius.toFloat(),
                        mDotPaint
                    )
                }
            }

            if (i == childCount - 1) {
                var lastChild: View? = parent.getChildAt(i - 1)
                if (null == lastChild) lastChild = child
                if (lastChild!!.bottom < child.bottom) {
                    top = child.bottom + mConfig.mBottomDistance
                    bottom =
                        child.bottom + if (mConfig.mStyle == STYLE_RESOURCE) mDrawable!!.intrinsicHeight else mConfig.mDotRadius
                } else {
                    top = lastChild.bottom + mConfig.mBottomDistance
                    bottom =
                        lastChild.bottom + if (mConfig.mStyle == STYLE_RESOURCE) mDrawable!!.intrinsicHeight else mConfig.mDotRadius
                }
                if (mConfig.mStyle == STYLE_RESOURCE) {
                    mDrawable!!.setBounds(drawableLeft, top, drawableRight, bottom)
                    mDrawable!!.draw(c)
                } else {
                    c.drawCircle(
                        drawableLeft.toFloat(),
                        top.toFloat(),
                        mConfig.mDotRadius.toFloat(),
                        mDotPaint
                    )
                }

                mTextPaint.getTextBounds(mConfig.mEnd, 0, mConfig.mEnd.length, mTextRect)
                mTextPaint.textSize = mConfig.mTextSize.toFloat()
                val textX = if (mConfig.mOnlyLeft)
                    mConfig.mOnlyLeftMarginLeft - mConfig.mLineWidth / 2 +mConfig.mDotRadius/2
                else
                    (parentWidth / 2).toFloat()
                c.drawText(
                    mConfig.mEnd,
                    (textX - mTextRect.width() / 2).toFloat(),
                    (bottom + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize).toFloat(),
                    mTextPaint
                )
            }
        }
    }

    private fun drawHorizontalLine(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val parentHeight = parent.measuredHeight
        val right: Int

        val childCount = parent.childCount
        if (childCount == 0) return
        val lastChild = parent.getChildAt(childCount - 1)
        if (childCount > 1) {
            val child = parent.getChildAt(childCount - 2)
            right =
                mConfig.mBottomDistance + if (child.right > lastChild.right) child.right else lastChild.right
        } else {
            right = mConfig.mBottomDistance + lastChild.right
        }

        c.drawLine(
            left.toFloat(),
            (parentHeight / 2).toFloat(),
            right.toFloat(),
            (parentHeight / 2).toFloat(),
            mLinePaint
        )
    }

    private fun drawHorizontalItem(c: Canvas, parent: RecyclerView) {
        val parentHeight = parent.measuredHeight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            var left = if (mConfig.mOnlyLeft) 0 else child.left + mConfig.mDotPaddingTop
            var right: Int
            val drawableLeft: Int

            if (mConfig.mStyle == STYLE_RESOURCE) {
                right = left + mDrawable!!.intrinsicWidth
                drawableLeft = parentHeight / 2 - mDrawable!!.intrinsicWidth / 2
                val drawableRight = parentHeight / 2 + mDrawable!!.intrinsicWidth / 2

                mDrawable!!.setBounds(drawableLeft, left, drawableRight, right)
                mDrawable!!.draw(c)
            } else {
                drawableLeft = if(mConfig.mOnlyLeft) 20 else parentHeight / 2

                if (mConfig.mDotInItemCenter) {
                    c.drawCircle(
                        ((child.left + child.right) / 2 + mConfig.mDotPaddingTop).toFloat(),
                        drawableLeft.toFloat(),
                        mConfig.mDotRadius.toFloat(),
                        mDotPaint
                    )
                } else {
                    c.drawCircle(
                        left.toFloat(),
                        drawableLeft.toFloat(),
                        mConfig.mDotRadius.toFloat(),
                        mDotPaint
                    )
                }
            }

            if (i == childCount - 1) {
                var lastChild: View? = parent.getChildAt(i - 1)
                if (null == lastChild) lastChild = child
                if (lastChild!!.right < child.right) {
                    left = child.right + mConfig.mBottomDistance
                    right =
                        child.right + if (mConfig.mStyle == STYLE_RESOURCE) mDrawable!!.intrinsicWidth else mConfig.mDotRadius
                } else {
                    left = lastChild.right + mConfig.mBottomDistance
                    right =
                        lastChild.right + if (mConfig.mStyle == STYLE_RESOURCE) mDrawable!!.intrinsicWidth else mConfig.mDotRadius
                }
                if (mConfig.mStyle == STYLE_RESOURCE) {
                    mDrawable!!.setBounds(left, drawableLeft, drawableRight, right)
                    mDrawable!!.draw(c)
                } else {
                    c.drawCircle(
                        left.toFloat(),
                        drawableLeft.toFloat(),
                        mConfig.mDotRadius.toFloat(),
                        mDotPaint
                    )
                }

                mTextPaint.getTextBounds(mConfig.mEnd, 0, mConfig.mEnd.length, mTextRect)
                mTextPaint.textSize = mConfig.mTextSize.toFloat()
                c.drawText(
                    mConfig.mEnd,
                    (right + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize).toFloat(),
                    (parentHeight / 2 + mTextRect.height() / 2).toFloat(),
                    mTextPaint
                )
            }
        }
    }

    class Config {
        @ItemStyle
        var mStyle = STYLE_DRAW
        @Orientation
        var mOrientation = VERTICAL
        var mTopDistance = 40
        var mItemInterval = 20
        var mItemPaddingLeft = mItemInterval//default
        var mItemPaddingRight = mItemInterval
        var mLineWidth = 4
        var mLineColor = Color.WHITE
        var mDotRes = R.drawable.timeline_dot
        var mDotPaddingTop = 20
        var mDotRadius = 5
        var mDotColor = Color.WHITE
        var mEnd = "END"
        var mTextColor = Color.WHITE
        var mTextSize = 18
        var mDotPaddingText = 10
        var mBottomDistance = 30
        var mDotInItemCenter = false
        var mOnlyLeft = false
        var mOnlyLeftMarginLeft = 20F
    }

    class Builder(private val mContext: Context) {
        private val mConfig: Config = Config()

        fun setOrientation(@Orientation orientation: Int): Builder {
            mConfig.mOrientation = orientation
            return this
        }

        fun setItemStyle(@ItemStyle itemStyle: Int): Builder {
            mConfig.mStyle = itemStyle
            return this
        }

        fun setOnlyLeft(onlyLeft :Boolean): Builder{
            mConfig.mOnlyLeft = onlyLeft
            return this
        }

        fun setOnlyLeftMarginLeft(marginLeft :Float): Builder{
            mConfig.mOnlyLeftMarginLeft = marginLeft
            return this
        }

        fun setTopDistance(distance: Float): Builder {
            mConfig.mTopDistance = Util.Dp2Px(mContext, distance)
            return this
        }

        fun setItemPaddingLeft(itemPaddingLeft: Float): Builder {
            mConfig.mItemPaddingLeft = Util.Dp2Px(mContext, itemPaddingLeft)
            return this
        }

        fun setItemPaddingRight(itemPaddingRight: Float): Builder {
            mConfig.mItemPaddingRight = Util.Dp2Px(mContext, itemPaddingRight)
            return this
        }

        fun setItemInterVal(interval: Float): Builder {
            mConfig.mItemInterval = Util.Dp2Px(mContext, interval)
            return this
        }

        fun setLineWidth(lineWidth: Float): Builder {
            mConfig.mLineWidth = Util.Dp2Px(mContext, lineWidth)
            return this
        }

        fun setLineColor(lineColor: Int): Builder {
            mConfig.mLineColor = lineColor
            return this
        }

        fun setDotPaddingTop(paddingTop: Int): Builder {
            mConfig.mDotPaddingTop = Util.Dp2Px(mContext, paddingTop.toFloat())
            return this
        }

        fun setDotRes(dotRes: Int): Builder {
            mConfig.mDotRes = dotRes
            return this
        }

        fun setDotRadius(dotRadius: Int): Builder {
            mConfig.mDotRadius = Util.Dp2Px(mContext, dotRadius.toFloat())
            return this
        }

        fun setDotColor(dotColor: Int): Builder {
            mConfig.mDotColor = dotColor
            return this
        }

        fun setTextColor(textColor: Int): Builder {
            mConfig.mTextColor = textColor
            return this
        }

        fun setTextSize(textSize: Float): Builder {
            mConfig.mTextSize = Util.Sp2Px(mContext, textSize)
            return this
        }

        fun setDotPaddingText(dotPaddingText: Float): Builder {
            mConfig.mDotPaddingText = Util.Dp2Px(mContext, dotPaddingText)
            return this
        }

        fun setDotInItemOrientationCenter(tag: Boolean): Builder {
            mConfig.mDotInItemCenter = tag
            return this
        }

        fun setBottomDistance(bottomDistance: Float): Builder {
            mConfig.mBottomDistance = Util.Dp2Px(mContext, bottomDistance)
            return this
        }

        fun setEndText(end: String): Builder {
            mConfig.mEnd = end
            return this
        }

        fun create(): DotItemDecoration {
            return DotItemDecoration(mContext, mConfig)
        }
    }

    companion object {
        const val STYLE_DRAW = 0
        const val STYLE_RESOURCE = 1

        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}