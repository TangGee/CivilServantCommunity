package com.mdove.civilservantcommunity.feed.view

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by MDove on 2019-09-09.
 */
class MainFeedPlaceHolderDrawable : Drawable() {

    private val mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        color = Color.WHITE
    }
    private val mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val RECT_SHADER = LinearGradient(24f, 24f, 700f, 24f, START_COLOR, END_COLOR, Shader.TileMode.CLAMP)
    private var mCanvasScale: Float = 0.toFloat()
    private var mScaledHeight: Int = 0

    private var mAlpha: Int = 0

    init {
        mBgPaint.color = BG_COLOR
        mRectPaint.shader = RECT_SHADER
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mCanvasScale = (right - left) / DESIGN_WIDTH
        mScaledHeight = ((bottom - top) / mCanvasScale).toInt()
    }

    override fun draw(canvas: Canvas) {
        val rect = bounds
        if (rect.width() <= 0 || rect.height() <= 0) {
            return
        }

        canvas.save()
        canvas.drawRect(rect, mBgPaint)
        canvas.translate(rect.left.toFloat(), rect.top.toFloat())
        canvas.scale(mCanvasScale, mCanvasScale)

        var top = 0
        while (top < mScaledHeight) {
            canvas.drawRect(RECT_1, mRectPaint)
            canvas.drawRect(RECT_2, mRectPaint)
            canvas.drawRect(RECT_3, mRectPaint)
            canvas.drawRect(RECT_4, mRectPaint)

            top += DESIGN_HEIGHT.toInt()
            canvas.translate(0f, DESIGN_HEIGHT)
        }

        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        mBgPaint.alpha = alpha
        mRectPaint.alpha = alpha * alpha / 255
        mAlpha = alpha
        invalidateSelf()
    }

    override fun getAlpha(): Int {
        return mAlpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    companion object {

        private const val DESIGN_WIDTH = 360f
        private const val DESIGN_HEIGHT = 88f

        private const val BG_COLOR = -0x1

        private const val START_COLOR = -0xf0f10
        private const val END_COLOR = -0x272728

        private val RECT_1 = Rect(12, 12, 280, 72)
        private val RECT_2 = Rect(12, 84, 240, 96)
        private val RECT_3 = Rect(12, 108, 240, 120)
        private val RECT_4 = Rect(256, 12, 348, 120)
    }
}
