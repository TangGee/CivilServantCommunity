package com.mdove.dependent.common.view.timeline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.mdove.dependent.common.R

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
    private var circleWidth = 0F
    private var strokeWidth = 0F

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StrokeCircle, defStyleAttr, 0)
        a?.let {
            circleWidth =
                a.getDimension(R.styleable.StrokeCircle_sc_circle_width, circleWidth)
            strokeWidth =
                a.getDimension(R.styleable.StrokeCircle_sc_stroke_width, strokeWidth)
            strokePaint.strokeWidth = strokeWidth
            circlePaint.color = a.getColor(R.styleable.StrokeCircle_sc_circle_color, Color.WHITE)
            strokePaint.color = a.getColor(R.styleable.StrokeCircle_sc_stroke_color, Color.WHITE)
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            canvas.save()
            canvas.translate(circleWidth / 2, circleWidth / 2)
            circleWidth.takeIf {
                it > 0
            }?.let {
                canvas.drawCircle(0F, 0F, it / 2 - strokeWidth / 2, circlePaint)
                canvas.drawCircle(0F, 0F, it / 2 - strokeWidth / 2, strokePaint)
            }
            canvas.restore()
        }
    }


    fun setStrokeColor(config: StrokeCircleConfig) {
        config.strokeWidth.takeIf {
            it > 0
        }?.let {
            strokeWidth = it
            strokePaint.strokeWidth = it
        }
        config.width.takeIf {
            it > 0
        }?.let {
            circleWidth = it
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
    val width: Float = 0F,
    val color: Int? = null
)