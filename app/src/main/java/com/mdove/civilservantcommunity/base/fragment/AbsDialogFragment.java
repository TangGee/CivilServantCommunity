package com.mdove.civilservantcommunity.base.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.mdove.civilservantcommunity.R;
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher;
import com.mdove.dependent.common.threadpool.MDovePoolKt;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.JobKt;

/**
 * Created by MDove on 2019-11-10.
 */
public abstract class AbsDialogFragment extends SafeShowDialogFragment implements ActivityLauncher, CoroutineScope {

    private CoroutineContext mainContext;

    public AbsDialogFragment() {
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
    }

    @NotNull
    @Override
    public CoroutineContext getCoroutineContext() {
        return mainContext;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        try {
            return super.show(transaction, tag);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mainContext = MDovePoolKt.FastMainScope();
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dialog_FullScreen);
        mLauncherHelper.onCreate(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null && isFull()) {
            WindowManager windowManager = window.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams windowParam = window.getAttributes();
            Point point = new Point();
            display.getSize(point);
            windowParam.width = point.x;
            windowParam.height = point.y;
            window.setAttributes(windowParam);
        }
    }

    protected boolean isFull() {
        return true;
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (WindowManager.BadTokenException e) {

        }
    }

    @Override
    public void onDismiss(@Nullable DialogInterface dialog) {
        try {
            super.onDismiss(dialog);
        } catch (WindowManager.BadTokenException e) {

        }
    }


    @Override
    public void dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (WindowManager.BadTokenException e) {

        }
    }

    private Helper mLauncherHelper = new Helper();

    @Override
    public void addResultHandler(@NotNull IResultHandler handler) {
        mLauncherHelper.addResultHandler(handler);
    }

    @Override
    public void removeResultHandler(@NotNull IResultHandler handler) {
        mLauncherHelper.removeResultHandler(handler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();
        if (context != null) {
            mLauncherHelper.handleActivityResult(this, requestCode, resultCode, data);
        }
    }

    @NotNull
    @Override
    public Deferred<Result> startActivityAsync(@NotNull Intent intent,
                                               @Nullable Bundle options) {
        return mLauncherHelper.startActivityForResult(this, intent, options);
    }
}