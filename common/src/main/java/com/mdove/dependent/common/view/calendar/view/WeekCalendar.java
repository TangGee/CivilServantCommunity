package com.mdove.dependent.common.view.calendar.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;

import com.mdove.dependent.common.view.calendar.adapter.BasePagerAdapter;
import com.mdove.dependent.common.view.calendar.adapter.WeekPagerAdapter;
import com.mdove.dependent.common.view.calendar.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekCalendar extends BaseCalendar {

    public WeekCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BasePagerAdapter getPagerAdapter(Context context, BaseCalendar baseCalendar) {
        return new WeekPagerAdapter(context, baseCalendar);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalWeek(startDate, endDate, type);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        return localDate.plusWeeks(count);
    }


}
