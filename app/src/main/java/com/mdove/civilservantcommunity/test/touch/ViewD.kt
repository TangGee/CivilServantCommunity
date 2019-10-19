package com.mdove.civilservantcommunity.test.touch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.mdove.civilservantcommunity.R

class ViewD @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.MultiLineChooseLayoutTagsStyle
) :TextView(context, attrs, defStyleAttr){
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d("mdove","ViewD -> dispatchTouchEvent")
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("mdove","ViewD -> onTouchEvent")
        return super.onTouchEvent(event)
    }
}