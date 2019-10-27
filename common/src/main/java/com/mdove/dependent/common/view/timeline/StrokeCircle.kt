package com.mdove.dependent.common.view.timeline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by MDove on 2019-10-27.
 */
class StrokeCircle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val strokePaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val circlePaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var radius = 0F
    private var strokeWidth = 0F

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            radius.takeIf {
                it > 0
            }?.let {
                canvas.drawCircle(0F, 0F, it, strokePaint)
                canvas.drawCircle(0F, 0F, it - strokeWidth, circlePaint)
            }
        }
    }


    fun setStrokeColor(config: StrokeCircleConfig) {
        config.strokeWidth.takeIf {
            it > 0
        }?.let {
            strokePaint.strokeWidth = it
        }
        config.radius.takeIf {
            it > 0
        }?.let {
            radius = it
        }
        config.color?.let {
            circlePaint.color = it
        }
        config.strokeColor?.let {
            strokePaint.color = it
        }
        invalidate()
    }
}

data class StrokeCircleConfig(
    val strokeWidth: Float = 0F,
    val strokeColor: Int? = null,
    val radius: Float = 0F,
    val color: Int? = null
)