package com.mdove.dependent.common.view.roundcorner

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * Created by MDove on 2019/9/10.
 */
open class RoundCornerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr), IRoundCornerView {

    private val helper = RoundCornerViewHelper(this)

    init {
        helper.init(context, attrs, defStyleAttr)
    }

    override fun superDraw(canvas: Canvas) {
        super.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        helper.onSizeChange(width, height)
    }

    override fun draw(canvas: Canvas?) {
        helper.draw(canvas)
    }
}