package com.mdove.dependent.common.view.roundcorner

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Created by MDove on 2019/9/10.
 */
class RoundCornerViewHelper(view: IRoundCornerView) : IRoundCornerViewHelper {
    private val impl = RoundCornerViewHelperImpl2(view)

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        impl.init(context, attrs, defStyleAttr)
    }

    override fun onSizeChange(width: Int, height: Int) {
        impl.onSizeChange(width, height)
    }

    override fun draw(canvas: Canvas?) {
        impl.draw(canvas)
    }
}