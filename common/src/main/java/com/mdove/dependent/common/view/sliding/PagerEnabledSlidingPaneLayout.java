package com.mdove.dependent.common.view.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.view.MotionEventCompat;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

public class PagerEnabledSlidingPaneLayout extends SlidingPaneLayout {

    //是否禁止侧滑
    private boolean prohibitSideSlip = false;
    private MDoveSlideableListener mListener;

    public PagerEnabledSlidingPaneLayout(Context context) {
        super(context, null);
    }

    public PagerEnabledSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PagerEnabledSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean getProhibitSideslip() {
        return prohibitSideSlip;
    }

    //在需要禁止或允许侧滑的地方调用该方法
    public void setProhibitSideslip(boolean prohibitSideslip) {
        this.prohibitSideSlip = prohibitSideslip;
    }

    //该方法可以拦截SlidingPaneLayout的触屏事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE:
                if (prohibitSideSlip && mListener != null && mListener.getSlideable()) {
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    ev2.setAction(MotionEvent.ACTION_CANCEL);
                    super.onInterceptTouchEvent(ev2);
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE:
                if (prohibitSideSlip && mListener != null && mListener.getSlideable()) {
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    ev2.setAction(MotionEvent.ACTION_CANCEL);
                    super.onInterceptTouchEvent(ev2);
                    return false;
                }
        }
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    public void setSlidingPanelListener(MDoveSlideableListener listener) {
        mListener = listener;
    }
}
