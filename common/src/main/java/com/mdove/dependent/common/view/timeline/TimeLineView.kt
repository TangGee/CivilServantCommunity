package com.mdove.dependent.common.view.timeline

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mdove.dependent.common.R
import com.mdove.dependent.common.utils.UIUtils

/**
 * Created by MDove on 2019-10-27.
 */
class TimeLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var tlvEndPointStrokeWidth = 0F
    private var tlvEndPointWidth = UIUtils.dip2Px(6)
    private var tlvEndPointColor = Color.WHITE
    private var tlvEndPointStrokeColor = Color.WHITE

    private var tlvTopPointStrokeWidth = 0F
    private var tlvTopPointWidth = UIUtils.dip2Px(6)
    private var tlvTopPointStrokeColor = Color.WHITE
    private var tlvTopPointColor = Color.WHITE

    private var tlvCenterPointStrokeWidth = UIUtils.dip2Px(1)
    private var tlvCenterPointWidth = UIUtils.dip2Px(8)
    private var tlvCenterPointColor = ContextCompat.getColor(context, R.color.blue_500)
    private var tlvCenterPointStrokeColor = Color.WHITE

    private var tlvLineStrokeColor = Color.WHITE
    private var tlvLineWidth = UIUtils.dip2Px(2)

    private var tlvTopMargin = 0F
    private var tlvEndMargin = 0F

    private var topPoint: StrokeCircle
    private var centerPoint: StrokeCircle
    private var endPoint: StrokeCircle
    private var lineeTop: View
    private var lineeBottom: View

    init {
        inflate(context, R.layout.view_time_line, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.TimeLineView, defStyleAttr, 0)
        a?.let {
            tlvCenterPointColor =
                a.getColor(R.styleable.TimeLineView_tlv_center_point_color, tlvCenterPointColor)
            tlvCenterPointStrokeColor = a.getColor(
                R.styleable.TimeLineView_tlv_center_point_stroke_color,
                tlvCenterPointStrokeColor
            )
            tlvCenterPointStrokeWidth = a.getDimension(
                R.styleable.TimeLineView_tlv_center_point_stroke_width,
                tlvCenterPointStrokeWidth
            )
            tlvCenterPointWidth =
                a.getDimension(R.styleable.TimeLineView_tlv_center_point_width, tlvCenterPointWidth)

            tlvTopPointStrokeWidth = a.getDimension(
                R.styleable.TimeLineView_tlv_top_point_stroke_width,
                tlvTopPointStrokeWidth
            )
            tlvTopPointWidth =
                a.getDimension(R.styleable.TimeLineView_tlv_top_point_width, tlvTopPointWidth)
            tlvTopPointStrokeColor = a.getColor(
                R.styleable.TimeLineView_tlv_top_point_stroke_color,
                tlvTopPointStrokeColor
            )
            tlvTopPointColor =
                a.getColor(R.styleable.TimeLineView_tlv_top_point_color, tlvTopPointColor)

            tlvEndPointStrokeWidth = a.getDimension(
                R.styleable.TimeLineView_tlv_end_point_stroke_width,
                tlvEndPointStrokeWidth
            )
            tlvEndPointWidth =
                a.getDimension(R.styleable.TimeLineView_tlv_end_point_width, tlvEndPointWidth)
            tlvEndPointStrokeColor = a.getColor(
                R.styleable.TimeLineView_tlv_end_point_stroke_color,
                tlvEndPointStrokeColor
            )
            tlvEndPointColor =
                a.getColor(R.styleable.TimeLineView_tlv_end_point_color, tlvEndPointColor)

            tlvLineWidth = a.getDimension(R.styleable.TimeLineView_tlv_line_width, tlvLineWidth)
            tlvLineStrokeColor =
                a.getColor(R.styleable.TimeLineView_tlv_line_color, tlvLineStrokeColor)

            tlvTopMargin = a.getDimension(
                R.styleable.TimeLineView_tlv_top_point_margin,
                tlvTopMargin
            )
            tlvEndMargin = a.getDimension(
                R.styleable.TimeLineView_tlv_end_point_margin,
                tlvEndMargin
            )
            it.recycle()

        }
        topPoint = findViewById(R.id.top_point)
        tlvTopMargin.takeIf {
            it > 0
        }?.let {
            topPoint.layoutParams = LayoutParams(
                it.toInt(),
                it.toInt()
            ).apply {
                topMargin = it.toInt()
            }
        }
        topPoint.setStrokeColor(
            StrokeCircleConfig(
                tlvTopPointStrokeWidth,
                tlvTopPointStrokeColor,
                tlvTopPointWidth,
                tlvTopPointColor
            )
        )

        endPoint = findViewById(R.id.end_point)
        tlvEndMargin.takeIf {
            it > 0
        }?.let {
            endPoint.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = it.toInt()
            }
        }
        endPoint.setStrokeColor(
            StrokeCircleConfig(
                tlvEndPointStrokeWidth,
                tlvEndPointStrokeColor,
                tlvEndPointWidth,
                tlvEndPointColor
            )
        )

        centerPoint = findViewById(R.id.center_point)
        centerPoint.setStrokeColor(
            StrokeCircleConfig(
                tlvCenterPointStrokeWidth,
                tlvCenterPointStrokeColor,
                tlvCenterPointWidth,
                tlvCenterPointColor
            )
        )

        lineeBottom = findViewById(R.id.line_bottom)
        lineeTop = findViewById(R.id.line_top)
        tlvLineWidth.takeIf {
            it > 0
        }?.let {
            lineeBottom.layoutParams = LayoutParams(
                it.toInt(),
                it.toInt()
            ).apply {
                width = it.toInt()
            }
            lineeTop.layoutParams = LayoutParams(
                it.toInt(),
                it.toInt()
            ).apply {
                width = it.toInt()
            }
        }
        try {
            lineeBottom.setBackgroundColor(tlvLineStrokeColor)
            lineeTop.setBackgroundColor(tlvLineStrokeColor)
        } catch (e: Exception) {

        }
    }
}