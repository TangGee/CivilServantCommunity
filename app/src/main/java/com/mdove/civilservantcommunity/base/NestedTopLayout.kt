package com.mdove.civilservantcommunity.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ViewCompat

/**
 * Created by MDove on 2019/9/1.
 */

class NestedTopLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent {

    private var mShowTop = false
    private var mHideTop = false
    private val mTopViewHeight = 800
    private val defaultMarginTop = 800

    override fun onFinishInflate() {
        super.onFinishInflate()
        scrollBy(0, -defaultMarginTop)
    }

    override fun onStartNestedScroll(@NonNull child: View, @NonNull target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(@NonNull child: View, @NonNull target: View, nestedScrollAxes: Int) {}

    override fun onStopNestedScroll(@NonNull target: View) {}

    override fun onNestedScroll(@NonNull target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {}

    override fun onNestedPreScroll(@NonNull target: View, dx: Int, dy: Int, @NonNull consumed: IntArray) {
        var dy = dy
        mShowTop = dy < 0 && Math.abs(scrollY) < mTopViewHeight && !target.canScrollVertically(-1)
        if (mShowTop) {
            if (Math.abs(scrollY + dy) > mTopViewHeight) {
                dy = -(mTopViewHeight - Math.abs(scrollY))
            }
        }
        mHideTop = dy > 0 && scrollY < 0
        if (mHideTop) {
            if (dy + scrollY > 0) {
                dy = -scrollY
            }
        }
        if (mShowTop || mHideTop) {
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    override fun onNestedFling(@NonNull target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return scrollY != 0
    }

    override fun onNestedPreFling(@NonNull target: View, velocityX: Float, velocityY: Float): Boolean {
        return scrollY != 0
    }

    override fun getNestedScrollAxes(): Int {
        return ViewCompat.SCROLL_AXIS_VERTICAL
    }
}
