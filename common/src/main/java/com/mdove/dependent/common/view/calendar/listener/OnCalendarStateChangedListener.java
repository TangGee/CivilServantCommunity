package com.mdove.dependent.common.view.calendar.listener;


import com.mdove.dependent.common.view.calendar.enumeration.CalendarState;

/**
 * 折叠日历状态变化回调
 */
public interface OnCalendarStateChangedListener {
    void onCalendarStateChange(CalendarState calendarState);
}
