package com.mdove.dependent.common.view.calendar.adapter;

import android.content.Context;


import com.mdove.dependent.common.view.calendar.enumeration.CalendarType;
import com.mdove.dependent.common.view.calendar.view.BaseCalendar;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekPagerAdapter extends BasePagerAdapter {


    public WeekPagerAdapter(Context context, BaseCalendar baseCalendar) {
        super(context, baseCalendar);
    }

    @Override
    protected LocalDate getPageInitializeDate(int position) {
        LocalDate localDate = mInitializeDate.plusDays((position - mPageCurrIndex) * 7);
        return localDate;
    }

    @Override
    protected CalendarType getCalendarType() {
        return CalendarType.WEEK;
    }

}
