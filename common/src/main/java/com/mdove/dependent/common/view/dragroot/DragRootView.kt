package com.mdove.dependent.common.view.dragroot

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import com.mdove.dependent.common.utils.UIUtils
import kotlin.math.abs

/**
 * Created by MDove on 2019/11/24.
 */
class DragRootView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    private var innerRlvs: MutableList<RecyclerView> = mutableListOf()
    private var mDownTime: Long = 0
    private var mDownPoint: FloatArray? = null
    private var mCanceled: Boolean = false
    private var mHasComputer = false

    private var mTouchSlop: Int = 0
    private val mDragCallback = MyDragCallback()
    private val mViewDragHelper = ViewDragHelper.create(this, mDragCallback)

    private var mInterceptClickListener: OnInterceptClickListener? = null
    private var mOnInterceptTouchListener: View.OnTouchListener? = null

    var exitInvoke: (() -> Unit)? = null

    // return true表示：没点到子View区域（此情况下会会掉外部点击的Listener）
    private var hasOutIntercept = false

    init {
        init(context)
    }

    private fun init(context: Context) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDragCallback.determinedDrag = false
                mDownTime = System.currentTimeMillis()
                mDownPoint = floatArrayOf(event.x, event.y)
                mCanceled = false
            }
            MotionEvent.ACTION_MOVE -> if (mTouchSlop < abs(event.x - mDownPoint!![0]) || mTouchSlop < abs(event.y - mDownPoint!![1])
            ) {
                mCanceled = true
            }
            else -> mCanceled = true
        }
        if (mInterceptClickListener != null && event.rawY < (UIUtils.getScreenHeight(context) - childMaxHeight())) {
            hasOutIntercept = true
            return true
        }
        hasOutIntercept = false
        return mViewDragHelper.shouldInterceptTouchEvent(event) || super.onInterceptTouchEvent(event)
    }

    private fun childMaxHeight(): Int {
        var max = 0
        children.forEach { child ->
            max = child.height.takeIf {
                it > max
            }?.let {
                it
            } ?: max
        }
        return max
    }

    fun setOnInterceptClickListener(listener: OnInterceptClickListener?) {
        mInterceptClickListener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!hasOutIntercept) {
            mViewDragHelper.processTouchEvent(event)
        } else {
            if (event.action == MotionEvent.ACTION_UP&&!mCanceled
                    && mTouchSlop >= abs(event.x - mDownPoint!![0])
                    && mTouchSlop >= abs(event.y - mDownPoint!![1])
            ) {
                mInterceptClickListener?.onClick(this)
            }
        }
        return true
    }

    override fun computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    interface OnInterceptClickListener {
        fun onClick(view: View): Boolean
    }

    private fun findAllRlv(view: ViewGroup) {
        if (mHasComputer) {
            return
        }
        val childCount = view.childCount
        for (index in 0..childCount) {
            val child = view.getChildAt(index)
            (child as? RecyclerView)?.let {
                innerRlvs.add(it)
            }
            if (child is ViewGroup) {
                findAllRlv(child)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        exitInvoke = null
    }

    private inner class MyDragCallback : ViewDragHelper.Callback() {

        internal var originTop: Int = 0
        internal var determinedDrag: Boolean = false

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (determinedDrag) {
                return false
            }
            var rlvIsTouch = false
            findAllRlv(this@DragRootView)
            mHasComputer = true
            if (innerRlvs.isNotEmpty()) {
                val rect =
                    Rect(0, 0, UIUtils.getScreenWidth(context), UIUtils.getScreenHeight(context))
                innerRlvs.forEach {
                    val isVisibleRect = it.getLocalVisibleRect(rect)
                    if (it.canScrollVertically(-1) && isVisibleRect) {
                        rlvIsTouch = true
                        return@forEach
                    }
                }
            }
            determinedDrag = true
            if (mViewDragHelper.viewDragState == ViewDragHelper.STATE_IDLE && !rlvIsTouch) {
                parent.requestDisallowInterceptTouchEvent(true)
                originTop = child.top
                return true
            }
            return false
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return height - paddingTop
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val innerView: Boolean
            innerView = when {
                yvel > 0 -> false
                yvel < 0 -> true
                else -> {
                    val topDelta = releasedChild.top - originTop
                    topDelta <= releasedChild.height / 2
                }
            }

            mViewDragHelper.smoothSlideViewTo(
                releasedChild,
                releasedChild.left,
                if (innerView) originTop else originTop + releasedChild.height
            )
            ViewCompat.postInvalidateOnAnimation(this@DragRootView)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return if (top < originTop) {
                originTop
            } else top
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            if (mViewDragHelper.viewDragState == ViewDragHelper.STATE_SETTLING
                && top == originTop + changedView.height
            ) {
                exitInvoke?.invoke()
            }
        }
    }
}
