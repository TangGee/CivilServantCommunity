package com.mdove.civilservantcommunity;

import android.app.Application;

import com.mdove.dependent.common.toast.ToastUtil;

/**
 * Created by MDove on 2019-09-08.
 */
public class MyApplication extends Application {
    private static Application sApp;

    public static Application getInst() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        ToastUtil.init(this);
    }
}
