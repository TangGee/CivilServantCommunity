package com.mdove.civilservantcommunity.view.timelineitemdecoration.util

import android.content.Context

import java.text.SimpleDateFormat
import java.util.Date

/**
 * *          _       _
 * *   __   _(_)_   _(_) __ _ _ __
 * *   \ \ / / \ \ / / |/ _` | '_ \
 * *    \ V /| |\ V /| | (_| | | | |
 * *     \_/ |_| \_/ |_|\__,_|_| |_|
 *
 *
 * Created by vivian on 2017/6/12.
 *
 * copy from https://github.com/vivian8725118/TimeLine
 */
object Util {
    /**
     * format date
     * @param time :ms
     * @return
     */
    fun LongtoStringFormat(time: Long): String {
        val currentTime = Date(time)
        val formatter = SimpleDateFormat("yyyy.MM.dd a HH:mm")
        return formatter.format(currentTime)
    }

    fun Dp2Px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun Sp2Px(context: Context, spValue: Float): Int {
        return (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }
}
