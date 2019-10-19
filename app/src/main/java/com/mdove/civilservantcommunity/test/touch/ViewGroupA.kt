package com.mdove.civilservantcommunity.test.touch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mdove.civilservantcommunity.R

class ViewGroupA @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.MultiLineChooseLayoutTagsStyle
) :FrameLayout(context, attrs, defStyleAttr){
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("mdove","ViewGroupA -> dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("mdove","ViewGroupA -> onInterceptTouchEvent")
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("mdove","ViewGroupA -> onTouchEvent")
        return super.onTouchEvent(event)
    }
}