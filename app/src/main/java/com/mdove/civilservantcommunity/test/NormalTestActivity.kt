package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.view.MotionEvent
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_normal_test.*

class NormalTestActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_test)

        et.setOnTouchListener { v, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN -> v.parent.requestDisallowInterceptTouchEvent(true)
                event.action == MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                event.action == MotionEvent.ACTION_CANCEL -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }
}