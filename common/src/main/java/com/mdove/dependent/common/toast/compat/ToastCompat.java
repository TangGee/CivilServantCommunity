package com.mdove.dependent.common.toast.compat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.lang.reflect.Field;

/**
 * Created by MDove on 2019-09-03.
 */
public final class ToastCompat {

    private final @NonNull
    Toast toast;

    private ToastCompat(@NonNull Toast base) {
        this.toast = base;
    }

    public static ToastCompat makeText(Context context, CharSequence text, int duration) {
        // We cannot pass the SafeToastContext to Toast.makeText() because
        // the View will unwrap the base context and we are in vain.
        @SuppressLint("ShowToast")
        Toast toast = Toast.makeText(context, text, duration);
        setContextCompat(toast.getView(), new SafeToastContext(context, toast));
        return new ToastCompat(toast);
    }

    public static ToastCompat makeText(Context context, @StringRes int resId, int duration)
        throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public @NonNull
    ToastCompat setBadTokenListener(@NonNull BadTokenListener listener) {
        final Context context = getView().getContext();
        if (context instanceof SafeToastContext) {
            ((SafeToastContext) context).setBadTokenListener(listener);
        }
        return this;
    }

    public void show() {
        toast.show();
    }

    public void cancel() {
        toast.cancel();
    }

    public void setDuration(int duration) {
        toast.setDuration(duration);
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        toast.setMargin(horizontalMargin, verticalMargin);
    }


    public void setText(int resId) {
        toast.setText(resId);
    }


    public void setText(CharSequence s) {
        toast.setText(s);
    }


    public void setView(View view) {
        toast.setView(view);
        setContextCompat(view, new SafeToastContext(view.getContext(), toast));
    }


    public float getHorizontalMargin() {
        return toast.getHorizontalMargin();
    }


    public float getVerticalMargin() {
        return toast.getVerticalMargin();
    }


    public int getDuration() {
        return toast.getDuration();
    }


    public int getGravity() {
        return toast.getGravity();
    }


    public int getXOffset() {
        return toast.getXOffset();
    }


    public int getYOffset() {
        return toast.getYOffset();
    }


    public View getView() {
        return toast.getView();
    }


    public @NonNull
    Toast getBaseToast() {
        return toast;
    }


    private static void setContextCompat(@NonNull View view, @NonNull Context context) {
        if (Build.VERSION.SDK_INT == 25) {
            try {
                Field field = View.class.getDeclaredField("mContext");
                field.setAccessible(true);
                field.set(view, context);
            } catch (Throwable throwable) {
            }
        }
    }
}
