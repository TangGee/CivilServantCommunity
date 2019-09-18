package com.mdove.civilservantcommunity.test

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.dependent.common.utils.UIUtils

class TextTouchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MultiLineChooseLayoutTagsStyle
) : FrameLayout(context, attrs, defStyleAttr) {
    var mHeight: Int = 0
    val MIN = UIUtils.dip2Px(200)
    lateinit var topView: View
    lateinit var touchView:View

    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = findViewById<View>(R.id.top)
        touchView = findViewById<View>(R.id.touch)
        val rlv = findViewById<RecyclerView>(R.id.below)
        rlv.isNestedScrollingEnabled = false
        rlv.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
        rlv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun getItemCount(): Int {
                return 10
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return ViewHolder1(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_text2,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (height >= UIUtils.getScreenHeight(context) / 10 * 8) {
            parent.requestDisallowInterceptTouchEvent(true)
            false
        } else {
            parent.requestDisallowInterceptTouchEvent(true)
            true
        }
    }

    var downY = 0F
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var offset = 0
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                offset = (event.y - downY).toInt()
                downY = event.y
            }
            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }

        return if (offset == 0){
            true
        }else {
            offset = if (offset + mHeight < MIN) {
                (MIN - mHeight).toInt()
            }else{
                offset
            }
            topView.layoutParams = FrameLayout.LayoutParams(topView.layoutParams).apply {
                topMargin = ((mHeight - MIN) / 2).toInt()
            }
            layoutParams.height = (mHeight + offset).toInt()
            layoutParams = layoutParams
            return true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = measuredHeight
    }

    inner class ViewHolder1(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {}
}