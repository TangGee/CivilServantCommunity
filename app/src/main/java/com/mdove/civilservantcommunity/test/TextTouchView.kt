package com.mdove.civilservantcommunity.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.dependent.common.utils.UIUtils

class TextTouchView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.MultiLineChooseLayoutTagsStyle
) : FrameLayout(context, attrs, defStyleAttr) {
    var mHeight:Int=0

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<RecyclerView>(R.id.rlv).isNestedScrollingEnabled=false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(height==UIUtils.getScreenHeight(context)){
            return false
        }
        return true
    }
    var downY=0F
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var offset=0F
        when(event!!.action){
            MotionEvent.ACTION_DOWN->{
                downY=event.y
            }
            MotionEvent.ACTION_MOVE->{
                Log.d("mdove","ACTION_MOVE-${event.y}-downY:$downY")
                offset+=event.y-downY
                downY=event.y
                Log.d("mdove","-----downY:$downY")
            }
        }
        layoutParams.height= (mHeight+offset).toInt()
        layoutParams=layoutParams
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight=height
    }
}