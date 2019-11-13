package com.mdove.dependent.common.view.calendar.listener;


import com.mdove.dependent.common.view.calendar.view.BaseCalendar;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/7/4.
 */

public interface OnCalendarChangedListener {
    void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate);
}
