package com.mdove.dependent.common.view.roundcorner

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import com.mdove.dependent.common.R

/**
 * Created by MDove on 2019/9/10.
 */
class RoundCornerViewHelperImpl2(private val view: IRoundCornerView) : IRoundCornerViewHelper {

    private var ltRadius = 0
    private var lbRadius = 0
    private var rtRadius = 0
    private var rbRadius = 0

    private val rect = RectF()
    private val radii = FloatArray(8)

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerView, defStyleAttr, 0)
        if (a != null) {
            val r = a.getDimensionPixelSize(R.styleable.RoundCornerView_allRadius, 0)
            ltRadius = a.getDimensionPixelSize(R.styleable.RoundCornerView_leftTopRadius, r)
            lbRadius = a.getDimensionPixelSize(R.styleable.RoundCornerView_leftBottomRadius, r)
            rtRadius = a.getDimensionPixelSize(R.styleable.RoundCornerView_rightTopRadius, r)
            rbRadius = a.getDimensionPixelSize(R.styleable.RoundCornerView_rightBottomRadius, r)

            radii[0] = ltRadius.toFloat()
            radii[1] = radii[0]

            radii[2] = rtRadius.toFloat()
            radii[3] = radii[2]

            radii[4] = rbRadius.toFloat()
            radii[5] = radii[4]

            radii[6] = lbRadius.toFloat()
            radii[7] = radii[6]

            a.recycle()
        }
        if (lbRadius > 0 || ltRadius > 0 || rtRadius > 0 || rbRadius > 0) {
            (view as? ViewGroup)?.setWillNotDraw(false)
        }
    }

    override fun onSizeChange(width: Int, height: Int) {
        if ((ltRadius == 0 && rtRadius == 0 && rbRadius == 0 && lbRadius == 0) || width == 0 || height == 0) {
            return
        }
        rect.set(0F, 0F, width.toFloat(), height.toFloat())
    }

    override fun draw(canvas: Canvas?) {
        val canvas = canvas ?: return
        if (ltRadius == 0 && rtRadius == 0 && lbRadius == 0 && rbRadius == 0) {
            view.superDraw(canvas)
        } else {
            val count = saveCanvas(canvas, rect, paint)

            path.reset()
            path.addRoundRect(rect, radii, Path.Direction.CW)
            canvas.drawPath(path, paint)

            paint.xfermode = xfermode
            saveCanvas(canvas, rect, paint)

            view.superDraw(canvas)

            paint.xfermode = null
            paint.colorFilter = null

            canvas.restoreToCount(count)
        }
    }

    private fun saveCanvas(canvas: Canvas, rect: RectF, paint: Paint): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(rect, paint)
        } else {
            canvas.saveLayer(rect, paint, Canvas.ALL_SAVE_FLAG)
        }
    }
}