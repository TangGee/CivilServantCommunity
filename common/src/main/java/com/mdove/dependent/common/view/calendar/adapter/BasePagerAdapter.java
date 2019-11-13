package com.mdove.dependent.common.view.calendar.adapter;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.mdove.dependent.common.view.calendar.baseview.CalendarView;
import com.mdove.dependent.common.view.calendar.baseview.CalendarView2;
import com.mdove.dependent.common.view.calendar.baseview.ICalendarView;
import com.mdove.dependent.common.view.calendar.enumeration.CalendarBuild;
import com.mdove.dependent.common.view.calendar.enumeration.CalendarType;
import com.mdove.dependent.common.view.calendar.view.BaseCalendar;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class BasePagerAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mPageSize;//总页数
    protected int mPageCurrIndex;
    protected LocalDate mInitializeDate;

    protected BaseCalendar mCalendar;

    public BasePagerAdapter(Context context, BaseCalendar baseCalendar) {
        this.mContext = context;
        this.mCalendar = baseCalendar;
        this.mInitializeDate = baseCalendar.getInitializeDate();
        this.mPageSize = baseCalendar.getCalendarPagerSize();
        this.mPageCurrIndex = baseCalendar.getCalendarCurrIndex();
    }

    @Override
    public int getCount() {
        return mPageSize;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ICalendarView iCalendarView;
        LocalDate pageInitializeDate = getPageInitializeDate(position);
        if (mCalendar.getCalendarBuild() == CalendarBuild.DRAW) {
            iCalendarView = new CalendarView(mContext, mCalendar, pageInitializeDate, getCalendarType());
        } else {
            iCalendarView = new CalendarView2(mContext, mCalendar, pageInitializeDate, getCalendarType());
        }
        ((View) iCalendarView).setTag(position);
        container.addView((View) iCalendarView);
        return iCalendarView;
    }

    /**
     * 每个页面的初始化日期
     *
     * @return
     */
    protected abstract LocalDate getPageInitializeDate(int position);

    /**
     * 获取是周日历还是月日历
     *
     * @return
     */
    protected abstract CalendarType getCalendarType();


}

