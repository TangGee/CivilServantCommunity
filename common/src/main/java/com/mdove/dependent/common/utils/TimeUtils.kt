package com.mdove.dependent.common.utils

import android.text.TextUtils
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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
        return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6)
    }

    val SECOND_IN_MILLIS: Long = 1000
    val MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60
    val HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60
    val DAY_IN_MILLIS = HOUR_IN_MILLIS * 24
    val FOUR_HOURS_IN_MILLIS = HOUR_IN_MILLIS * 4
    val WEEK_IN_MILLIS = DAY_IN_MILLIS * 7
    val MONTH_IN_MILLIS = DAY_IN_MILLIS * 30

    /**
     * This constant is actually the length of 364 days, not of a year!
     */
    val YEAR_IN_MILLIS = WEEK_IN_MILLIS * 52
    private val DEFAULT_DATE_FORMAT = ThreadLocalDateFormat("yyyy-MM-dd HH:mm:ss")

    class ThreadLocalDateFormat(private val mDatePattern: String) : ThreadLocal<DateFormat>() {

        override fun initialValue(): DateFormat? {
            return SimpleDateFormat(mDatePattern, Locale.US)
        }
    }

    fun getDay(time: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    fun getYear(time: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        return cal.get(Calendar.YEAR)
    }

    fun formatMinsS(timestamp: Long): String {
        return if (getHour(timestamp) <= 0) {
            String.format(
                "%dmins%ds",
                TimeUnit.MILLISECONDS.toMinutes(timestamp),
                TimeUnit.MILLISECONDS.toSeconds(timestamp) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                        timestamp
                    )
                )
            )
        } else simpleFormat(TimeZone.getDefault().displayName, timestamp, "HH:mm:ss")
    }

    fun getHour(time: Long): Int {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZone.getDefault().displayName))
        calendar.timeInMillis = time
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getHour(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getMonth(time: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        return cal.get(Calendar.MONTH) + 1
    }

    fun getDateChineseYMD(time: Date): String {
        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(time)
    }

    fun getDateChineseYMD(time: Long): String {
        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(time)
    }


    fun formatDefault(time: Long): String {
        return DEFAULT_DATE_FORMAT.get()!!.format(Date(time))
    }

    fun simpleFormat(@Nullable timeZone: String?, time: Long, @NonNull datePattern: String): String {
        val date = Date(time)
        val format = SimpleDateFormat(datePattern)
        if (timeZone != null) {
            format.setTimeZone(TimeZone.getTimeZone(timeZone))
        }
        return format.format(date)
    }

    fun getYearMonth(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM")
        val date = Date(time)
        return format.format(date)
    }

    fun simpleFormat(time: Long, @NonNull datePattern: String): String {
        val date = Date(time)
        val format = SimpleDateFormat(datePattern)
        return format.format(date)
    }

    fun simpleFormat(@NonNull datePattern: String): String {
        val date = Date()
        val format = SimpleDateFormat(datePattern)
        return format.format(date)
    }

    fun isInDay(time: Long): Boolean {
        val current = System.currentTimeMillis()
        return current - time in 1..DAY_IN_MILLIS
    }

    fun isFourHours(time: Long): Boolean {
        val current = System.currentTimeMillis()
        return current - time in 1..FOUR_HOURS_IN_MILLIS
    }

    fun isSameDay(time1: Long, time2: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = Date(time1)
        val day1 = calendar.get(Calendar.DAY_OF_YEAR)
        calendar.time = Date(time2)
        val day2 = calendar.get(Calendar.DAY_OF_YEAR)
        return day1 == day2
    }

    fun isToday(time: Long): Boolean {
        return isSameDay(time, System.currentTimeMillis())
    }

    fun getHourOfDay(): Int {
        val c = Calendar.getInstance()
        return c.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 获取晚上9点半的时间戳
     *
     * @return
     */
    fun getTimes(day: Int, hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, day)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getTimes(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getMinute(timestamp: Long): Int {
        val c = Calendar.getInstance()
        c.timeInMillis = timestamp
        return c.get(Calendar.MINUTE)
    }

    fun getDayInMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getDayOfWeek(timeZone: String, timestamp: Long): Int {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone))
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getSimpleMonthE(abbrev: Boolean): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        return parseMonthE(month, abbrev)
    }

    fun getSimpleMonthC(abbrev: Boolean): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        return parseMonthC(month, abbrev)
    }

    fun getDayOfWeek(time: Long): String {
        val dayNames = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
        val c = Calendar.getInstance()
        c.time = Date(time)
        return dayNames[c.get(Calendar.DAY_OF_WEEK) - 1]
    }

    //HH为24小时进制
    fun getDateChinese(time: Date): String {
        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(time)
    }

    fun getDateChineseNoH(time: Long?): String {
        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(time)
    }

    fun getDateChinese(time: Long?): String {
        val format = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        return format.format(time ?: System.currentTimeMillis())
    }

    fun getHourM(time: Long?): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(time ?: System.currentTimeMillis())
    }

    fun getHourMChinese(time: Long?): String {
        val format = SimpleDateFormat("HH小时mm分")
        return format.format(time ?: System.currentTimeMillis())
    }

    fun getDateChinese(): String {
        val format = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        return format.format(Date())
    }

    private fun parseMonthE(month: Int, abbrev: Boolean): String {
        when (month) {
            Calendar.JANUARY -> return if (abbrev) "Ja." else "January"
            Calendar.FEBRUARY -> return if (abbrev) "Feb." else "February"
            Calendar.MARCH -> return if (abbrev) "Mar." else "March"
            Calendar.APRIL -> return if (abbrev) "Apr." else "April"
            Calendar.MAY -> return if (abbrev) "May." else "May"
            Calendar.JUNE -> return if (abbrev) "Jun." else "June"
            Calendar.JULY -> return if (abbrev) "Jul." else "July"
            Calendar.AUGUST -> return if (abbrev) "Aug." else "August"
            Calendar.SEPTEMBER -> return if (abbrev) "Sep." else "September"
            Calendar.OCTOBER -> return if (abbrev) "Oct." else "October"
            Calendar.NOVEMBER -> return if (abbrev) "Nov." else "November"
            Calendar.DECEMBER -> return if (abbrev) "Dec." else "December"
            else -> return ""
        }
    }

    private fun parseMonthC(month: Int, allChinese: Boolean): String {
        when (month) {
            Calendar.JANUARY -> return if (!allChinese) "1月" else "一月"
            Calendar.FEBRUARY -> return if (!allChinese) "2月" else "二月"
            Calendar.MARCH -> return if (!allChinese) "3月" else "三月"
            Calendar.APRIL -> return if (!allChinese) "4月" else "四月"
            Calendar.MAY -> return if (!allChinese) "5月" else "五月"
            Calendar.JUNE -> return if (!allChinese) "6月" else "六月"
            Calendar.JULY -> return if (!allChinese) "7月" else "七月"
            Calendar.AUGUST -> return if (!allChinese) "8月" else "八月"
            Calendar.SEPTEMBER -> return if (!allChinese) "9月" else "九月"
            Calendar.OCTOBER -> return if (!allChinese) "10月" else "十月"
            Calendar.NOVEMBER -> return if (!allChinese) "11月" else "十一月"
            Calendar.DECEMBER -> return if (!allChinese) "12月" else "十二月"
            else -> return ""
        }
    }

    fun getSimpleWeek(abbrev: Boolean): String {
        val calendar = Calendar.getInstance()
        val weekday = calendar.get(Calendar.DAY_OF_WEEK)
        return parseWeek(weekday, abbrev)
    }

    private fun parseWeek(weekday: Int, abbrev: Boolean): String {
        val dayInWeek: String
        when (weekday) {
            Calendar.SUNDAY -> dayInWeek = if (abbrev) "周日" else "星期天"
            Calendar.MONDAY -> dayInWeek = if (abbrev) "周一" else "星期一"
            Calendar.TUESDAY -> dayInWeek = if (abbrev) "周二" else "星期二"
            Calendar.WEDNESDAY -> dayInWeek = if (abbrev) "周三" else "星期三"
            Calendar.THURSDAY -> dayInWeek = if (abbrev) "周四" else "星期四"
            Calendar.FRIDAY -> dayInWeek = if (abbrev) "周五" else "星期五"
            Calendar.SATURDAY -> dayInWeek = if (abbrev) "周六" else "星期六"
            else -> dayInWeek = if (abbrev) "周日" else "星期天"
        }
        return dayInWeek
    }

    fun getSimpleWeek(@Nullable timeZone: String, timestamp: Long, abbrev: Boolean): String {
        var timeZone = timeZone
        if (TextUtils.isEmpty(timeZone)) {
            timeZone = TimeZone.getDefault().displayName
        }
        val dayOfWeek = getDayOfWeek(timeZone, timestamp)
        return parseWeek(dayOfWeek, abbrev)
    }
}