package com.mdove.dependent.common.view.calendar.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;

import com.mdove.dependent.common.view.calendar.adapter.BasePagerAdapter;
import com.mdove.dependent.common.view.calendar.adapter.MonthPagerAdapter;
import com.mdove.dependent.common.view.calendar.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthCalendar extends BaseCalendar {

    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BasePagerAdapter getPagerAdapter(Context context, BaseCalendar baseCalendar) {
        return new MonthPagerAdapter(context,baseCalendar);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }
}
