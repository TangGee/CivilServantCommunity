package com.mdove.dependent.common.view.calendar.adapter;

import android.content.Context;

import com.mdove.dependent.common.view.calendar.enumeration.CalendarType;
import com.mdove.dependent.common.view.calendar.view.BaseCalendar;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthPagerAdapter extends BasePagerAdapter {

    public MonthPagerAdapter(Context context, BaseCalendar baseCalendar) {
        super(context, baseCalendar);
    }

    @Override
    protected LocalDate getPageInitializeDate(int position) {
        LocalDate localDate = mInitializeDate.plusMonths(position - mPageCurrIndex);
        return localDate;
    }

    @Override
    protected CalendarType getCalendarType() {
        return CalendarType.MONTH;
    }
}
