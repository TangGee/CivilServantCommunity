package com.mdove.civilservantcommunity.feed.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by MDove on 2019-09-09.
 */
class PlaceHolderRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var mDrawable = MainFeedPlaceHolderDrawable()
    private var showPlaceHolder = true

    override fun onDraw(c: Canvas?) {
        super.onDraw(c)
        if (showPlaceHolder && c != null) {
            mDrawable.draw(c)
        }
    }

    fun updateEmptyUI() {
        showPlaceHolder = true
        adapter?.let {
            if (it.itemCount > 0) {
                showPlaceHolder = false
            }
        }
        invalidate()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        try {
            super.onLayout(changed, l, t, r, b)
            mDrawable.setBounds(0, 0, width, height)
        } catch (e: Exception) {
        }
    }
}