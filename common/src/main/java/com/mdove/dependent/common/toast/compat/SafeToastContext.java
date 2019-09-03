package com.mdove.dependent.common.toast.compat;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.logging.Logger;

/**
 * Created by MDove on 2019-09-03.
 */
final class SafeToastContext extends ContextWrapper {

    private @NonNull
    Toast toast;

    private @Nullable
    BadTokenListener badTokenListener;


    SafeToastContext(@NonNull Context base, @NonNull Toast toast) {
        super(base);
        this.toast = toast;
    }


    @Override
    public Context getApplicationContext() {
        return new ApplicationContextWrapper(getBaseContext().getApplicationContext());
    }


    public void setBadTokenListener(@NonNull BadTokenListener badTokenListener) {
        this.badTokenListener = badTokenListener;
    }


    private final class ApplicationContextWrapper extends ContextWrapper {

        private ApplicationContextWrapper(@NonNull Context base) {
            super(base);
        }


        @Override
        public Object getSystemService(@NonNull String name) {
            if (Context.WINDOW_SERVICE.equals(name)) {
                // noinspection ConstantConditions
                return new WindowManagerWrapperForToast((WindowManager) getBaseContext().getSystemService(name));
            }
            return super.getSystemService(name);
        }
    }


    private final class WindowManagerWrapperForToast implements WindowManager {

        private static final String TAG = "WindowManagerWrapperForToast";
        private final @NonNull
        WindowManager base;


        private WindowManagerWrapperForToast(@NonNull WindowManager base) {
            this.base = base;
        }


        @Override
        public Display getDefaultDisplay() {
            return base.getDefaultDisplay();
        }


        @Override
        public void removeViewImmediate(View view) {
            base.removeViewImmediate(view);
        }


        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {
            try {
//                Logger.d(TAG, "WindowManager's addView(view, params) has been hooked.");
                base.addView(view, params);
            } catch (BadTokenException e) {
                if (badTokenListener != null) {
                    badTokenListener.onBadTokenCaught(toast);
                }
//                CrashlyticsUtils.safeLogException(TAG + " " + e.toString());
            } catch (Throwable throwable) {
//                CrashlyticsUtils.safeLogException(TAG + " " + throwable.toString());
            }
        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
            base.updateViewLayout(view, params);
        }

        @Override
        public void removeView(View view) {
            base.removeView(view);
        }
    }
}
