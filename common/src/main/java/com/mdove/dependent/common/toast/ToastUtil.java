package com.mdove.dependent.common.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.mdove.dependent.common.R;
import com.mdove.dependent.common.toast.compat.ToastCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by MDove on 2019-09-03.
 */
public class ToastUtil {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Toast.LENGTH_LONG, Toast.LENGTH_SHORT})
    @interface Duration{}

    private static Context sAppContext;
//    private static int sYOffset;

    public static void init(Context context) {
        sAppContext = context.getApplicationContext();
//        sYOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, sAppContext.getResources().getDisplayMetrics());
    }

    public static void toast(int text, @Duration int duration) {
        toast(0, null, text, null, duration);
    }

    public static void toast(String text, @Duration int duration) {
        toast(0, null, 0, text, duration);
    }

    public static void toast(int icon, int text, @Duration int duration) {
        toast(icon, null, text, null, duration);
    }

    public static void toast(int icon, String text, @Duration int duration) {
        toast(icon, null, 0, text, duration);
    }

    public static void toast(Drawable iconDrawable, int text, @Duration int duration) {
        toast(0, iconDrawable, text, null, duration);
    }

    public static void toast(Drawable iconDrawable, String text, @Duration int duration) {
        toast(0, iconDrawable, 0, text, duration);
    }

    // ================ //

    public static void toast(int iconRes, Drawable iconDrawable, int textRes, String textStr, @Duration int duration) {
        toast(iconRes, iconDrawable, textRes, textStr, duration, Gravity.CENTER, 0, 0);
    }

    public static void toast(final int iconRes, final Drawable iconDrawable, final int textRes, final String textStr, final @Duration int duration, final int gravity, final int xOffset, final int yOffset) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doToast(iconRes, iconDrawable, textRes, textStr, duration, gravity, xOffset, yOffset);
        } else {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    doToast(iconRes, iconDrawable, textRes, textStr, duration, gravity, xOffset, yOffset);
                }
            });
        }
    }

    @SuppressLint("ShowToast")
    private static void doToast(int iconRes, Drawable iconDrawable, int textRes, String textStr, @Duration int duration, int gravity, int xOffset, int yOffset) {
        ToastCompat toast = null;
        if (iconRes > 0 || iconDrawable != null) {
            TextView textView = getTextView(sAppContext);
            if (iconRes > 0) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, iconRes, 0, 0);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, iconDrawable, null, null);
            }
            if (textStr != null || textRes == 0) {
                textView.setText(textStr);
            } else {
                textView.setText(textRes);
            }
            toast = ToastCompat.makeText(sAppContext, "", duration);
            toast.setView(textView);
            toast.setDuration(duration);
        } else if(textRes > 0 || textStr != null) {
            if (textStr != null) {
                toast = ToastCompat.makeText(sAppContext, textStr, duration);
            } else {
                toast = ToastCompat.makeText(sAppContext, textRes, duration);
            }
        }

        if (toast != null) {
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        }
    }

    @NonNull
    private static TextView getTextView(Context context) {
        TextView textView = new TextView(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);

        textView.setPadding(dp_10, dp_10, dp_10, dp_10);
        textView.setCompoundDrawablePadding(dp_10);
        textView.setGravity(Gravity.CENTER);

        Typeface type = Typeface.SANS_SERIF;
        textView.setTypeface(type);

        textView.setTextColor(0xffffffff);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        textView.setBackgroundResource(R.drawable.bg_slide_hint);
        return textView;
    }
}
