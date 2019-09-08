package com.mdove.dependent.common.view.tablayout

import android.animation.TimeInterpolator
import android.graphics.PointF

class CubicBezierInterpolator @Throws(IllegalArgumentException::class) constructor(protected var start: PointF, protected var end: PointF)
    : TimeInterpolator {

    protected var a = PointF()
    protected var b = PointF()
    protected var c = PointF()

    init {
        if (start.x < 0 || start.x > 1)
            throw IllegalArgumentException("startX value must be in the range [0, 1]")
        if (end.x < 0 || end.x > 1)
            throw IllegalArgumentException("endX value must be in the range [0, 1]")
    }

    @JvmOverloads
    constructor(startX: Float = getCurveParameters(
        STANDARDINOUT
    )[0], startY: Float = getCurveParameters(
        STANDARDINOUT
    )[1], endX: Float = getCurveParameters(
        STANDARDINOUT
    )[2], endY: Float = getCurveParameters(
        STANDARDINOUT
    )[3]) : this(PointF(startX, startY), PointF(endX, endY))

    constructor(curve: Int) : this(
        getCurveParameters(
            curve
        )[0], getCurveParameters(
            curve
        )[1], getCurveParameters(
            curve
        )[2], getCurveParameters(
            curve
        )[3])

    override fun getInterpolation(time: Float): Float {
        return getBezierCoordinateY(getXForTime(time))
    }

    protected fun getBezierCoordinateY(time: Float): Float {
        c.y = 3 * start.y
        b.y = 3 * (end.y - start.y) - c.y
        a.y = 1f - c.y - b.y
        return time * (c.y + time * (b.y + time * a.y))
    }

    protected fun getXForTime(time: Float): Float {
        var x = time
        var z: Float
        for (i in 1..13) {
            z = getBezierCoordinateX(x) - time
            if (Math.abs(z) < 1e-3) break
            x -= z / getXDerivate(x)
        }
        return x
    }

    private fun getXDerivate(t: Float): Float {
        return c.x + t * (2 * b.x + 3f * a.x * t)
    }

    private fun getBezierCoordinateX(time: Float): Float {
        c.x = 3 * start.x
        b.x = 3 * (end.x - start.x) - c.x
        a.x = 1f - c.x - b.x
        return time * (c.x + time * (b.x + time * a.x))
    }

    companion object {

        protected fun getCurveParameters(curve: Int): FloatArray {
            when (curve) {
                LINEAR -> return floatArrayOf(0f, 0f, 1f, 1f)

                SINEEASEIN -> return floatArrayOf(0.47f, 0f, 0.745f, 0.715f)
                SINEEASEOUT -> return floatArrayOf(0.39f, 0.575f, 0.565f, 1f)
                SINEEASEINOUT -> return floatArrayOf(0.445f, 0.05f, 0.55f, 0.95f)

                QUADEASEIN -> return floatArrayOf(0.26f, 0f, 0.6f, 0.2f)
                QUADEASEOUT -> return floatArrayOf(0.4f, 0.8f, 0.74f, 1f)
                QUADEASEINOUT -> return floatArrayOf(0.48f, 0.04f, 0.52f, 0.96f)

                CUBICEASEIN -> return floatArrayOf(0.4f, 0f, 0.68f, 0.06f)
                CUBICEASEOUT -> return floatArrayOf(0.32f, 0.94f, 0.6f, 1f)
                CUBICEASEINOUT -> return floatArrayOf(0.66f, 0f, 0.34f, 1f)

                QUARTEASEIN -> return floatArrayOf(0.52f, 0f, 0.74f, 0f)
                QUARTEASEOUT -> return floatArrayOf(0.26f, 1f, 0.48f, 1f)
                QUARTEASEINOUT -> return floatArrayOf(0.76f, 0f, 0.24f, 1f)

                QUINTEASEIN -> return floatArrayOf(0.64f, 0f, 0.78f, 0f)
                QUINTEASEOUT -> return floatArrayOf(0.22f, 1f, 0.36f, 1f)
                QUINTEASEINOUT -> return floatArrayOf(0.84f, 0f, 0.16f, 1f)

                EXPOEASEIN -> return floatArrayOf(0.66f, 0f, 0.86f, 0f)
                EXPOEASEOUT -> return floatArrayOf(0.14f, 1f, 0.34f, 1f)
                EXPOEASEINOUT -> return floatArrayOf(0.9f, 0f, 0.1f, 1f)
                STANDARDINOUT -> return floatArrayOf(0.15f, 0.12f, 0f, 1f)
                CUBICOUT -> return floatArrayOf(0.215f, 0.61f, 0.355f, 1f)
                CUBICIN -> return floatArrayOf(0.55f, 0.055f, 0.675f, 0.19f)
                else -> return floatArrayOf(0.15f, 0.12f, 0f, 1f)
            }
        }

        const val LINEAR = 0

        const val SINEEASEIN = 1
        const val SINEEASEOUT = 2
        const val SINEEASEINOUT = 3

        const val QUADEASEIN = 4
        const val QUADEASEOUT = 5
        const val QUADEASEINOUT = 6

        const val CUBICEASEIN = 7
        const val CUBICEASEOUT = 8
        const val CUBICEASEINOUT = 9

        const val QUARTEASEIN = 10
        const val QUARTEASEOUT = 11
        const val QUARTEASEINOUT = 12

        const val QUINTEASEIN = 13
        const val QUINTEASEOUT = 14
        const val QUINTEASEINOUT = 15

        const val EXPOEASEIN = 16
        const val EXPOEASEOUT = 17
        const val EXPOEASEINOUT = 18

        const val STANDARDINOUT = 19

        const val CUBICOUT = 20

        const val CUBICIN = 21
    }
}