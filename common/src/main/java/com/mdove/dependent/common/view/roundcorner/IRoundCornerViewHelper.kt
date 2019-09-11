package com.mdove.dependent.common.view.roundcorner

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Created by MDove on 2019/9/10.
 */
interface IRoundCornerViewHelper {
    fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0)
    fun onSizeChange(width: Int, height: Int)
    fun draw(canvas: Canvas?)
}