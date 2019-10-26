package com.mdove.dependent.common.view.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mdove.dependent.common.InterceptType;

public class SwipeRefreshLayoutCustom extends SwipeRefreshLayout {

    private OnListContentCallback mListContentCallback;

    public SwipeRefreshLayoutCustom(Context context) {
        super(context);
    }

    public SwipeRefreshLayoutCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        return super.canChildScrollUp() && mListContentCallback != null && !mListContentCallback.isScrolledToTop();
    }

    public void setListContentCallback(OnListContentCallback callback) {
        mListContentCallback = callback;
    }

    public interface OnListContentCallback {

        boolean isScrolledToTop();

    }

    private int mInterceptType = InterceptType.DEFAULT;

    public void setInterceptType(@InterceptType int type){
        mInterceptType = type;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mInterceptType == InterceptType.INTERCEPT_TOUCHEVENT){
            return true;
        } else if(mInterceptType == InterceptType.NO_INTERCEPT_TOUCHEVENT){
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
