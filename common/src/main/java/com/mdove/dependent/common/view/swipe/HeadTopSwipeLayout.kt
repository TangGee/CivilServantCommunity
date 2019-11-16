package com.mdove.dependent.common.view.swipe

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * Created by MDove on 2019/10/26.
 *
 */
open class HeadTopSwipeLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SwipeRefreshLayoutCustom(context, attrs) {

    var mLastX: Float = 0.0f
    var mLastY: Float = 0.0f

    private var mTouchSlop: Int = 0
    private var mPrevX: Float = 0.toFloat()

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = ev.x
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY

                val  eventX = ev.x
                val xDiff = Math.abs(eventX - mPrevX);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                if (xDiff > mTouchSlop + 60) {
                    return false
                }
                //if deltaX >= deltaY, swipe layout should not intercept the touch event.
                if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        mLastX = ev.x
        mLastY = ev.y
        return super.onTouchEvent(ev)
    }
}
