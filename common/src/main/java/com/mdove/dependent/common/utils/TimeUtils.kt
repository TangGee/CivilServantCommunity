package com.mdove.dependent.common.utils

import java.util.*


/**
 * Created by MDove on 2019-09-22.
 */
object TimeUtils {

    fun isSameDay(date1: Long, date2: Long? = null): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = Date(date1)
        val cal2 = Calendar.getInstance()
        cal2.time = date2?.let {
            Date(it)
        } ?: Date()
        return isSameDay(cal1, cal2)
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
    }
}