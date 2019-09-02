package com.mdove.civilservantcommunity.view.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.mdove.civilservantcommunity.R;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

public class StatusBarUtil {
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
//            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
//            ViewGroup rootView = (ViewGroup) content.getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(true);

        }
    }

    public static void setColor(Activity activity, int color) {
        try {
            if (Build.VERSION.SDK_INT > VERSION_CODES.KITKAT) {
                // 设置状态栏透明
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 生成一个状态栏大小的矩形
                View statusView = createStatusView(activity, color);
                // 添加 statusView 到布局中
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                decorView.addView(statusView);
                // 设置根布局的参数
                ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
                rootView.setFitsSystemWindows(true);
                rootView.setClipToPadding(true);
            }
        } catch (Throwable ignore) {}
    }

    private static View createStatusView(Activity activity, int color) {
        int statusBarHeight = getStatausBarHeight(activity);
        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(ContextCompat.getColor(activity, color));
        return statusView;
    }

    // 获得状态栏高度
    public static int getStatausBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }

    public static void showStatusBar(Activity activity, boolean show) {
        if (activity == null) {
            return;
        }
        /*
        if (show) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        */
        try {
            Window window = activity.getWindow();
            if (show) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                View decorView = window.getDecorView();
                int uiOptions =
                        show ?
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE:
                                SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
                if (rootView != null) {
//                rootView.setFitsSystemWindows(show);
//                rootView.setClipToPadding(!show);
                    // a little trick here
                    // 由于采用沉浸式状态栏 保证splash到主页不会发生跳动 这样造成content顶到最上面 自带一个paddingtop，状态栏盖在上面，但是一次横竖屏切换回来，content又变为在状态栏下面
                    // 所以这里只需要设置content的padding是0即可  在找到解决首页沉浸式方案之前 暂时 这么做
                    rootView.setPadding(rootView.getPaddingLeft(),
                            0,
                            rootView.getPaddingRight(),
                            rootView.getPaddingBottom());
                }

                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Throwable ignore) {}
    }

    public static void showVenusStatusBar(Activity activity, boolean withFullFlag){
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                Window window = activity.getWindow();
                final boolean hasFullScreenFlag = window.getAttributes() != null &&
                        (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
                if (hasFullScreenFlag) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                window.setStatusBarColor(activity.getResources().getColor(R.color.white, null));
                View decorView = window.getDecorView();
                int uiOptions = SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                if (withFullFlag) {
                    uiOptions |= SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                }
                ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
                if (rootView != null) {
//                rootView.setFitsSystemWindows(show);
//                rootView.setClipToPadding(!show);
                    // a little trick here
                    // 由于采用沉浸式状态栏 保证splash到主页不会发生跳动 这样造成content顶到最上面 自带一个paddingtop，状态栏盖在上面，但是一次横竖屏切换回来，content又变为在状态栏下面
                    // 所以这里只需要设置content的padding是0即可  在找到解决首页沉浸式方案之前 暂时 这么做
                    rootView.setPadding(rootView.getPaddingLeft(),
                            0,
                            rootView.getPaddingRight(),
                            rootView.getPaddingBottom());
                }
                decorView.setSystemUiVisibility(uiOptions);
            }
        }catch (Throwable ignore) {}

    }

    public static void setVenusStatusBarColor(Activity activity,@ColorInt int statusColor) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                Window window = activity.getWindow();
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                window.setStatusBarColor(statusColor);
                //设置状态栏图标深色
                window.getDecorView().setSystemUiVisibility(
                        SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } catch (Throwable ignore) {
        }
    }

    public static void setVenusDetailStatusBarStyle(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                int flags = SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

            }
        } catch (Throwable ignore) {
        }

    }

    public static void setVenusDetailImmersStatusBarStyle(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                int flags = SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);

                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

            }
        } catch (Throwable ignore) {
        }

    }


    public static void hideStatusBar(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().getDecorView()
                        .setSystemUiVisibility(
                                SYSTEM_UI_FLAG_FULLSCREEN |
                                        SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                        SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                        SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } catch (Throwable ignore) {
        }
    }

    public static void showOrHideNaviBar(Activity activity, boolean show) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (rootView != null) {
//                rootView.setFitsSystemWindows(show);
//                rootView.setClipToPadding(!show);
            // a little trick here
            // 由于采用沉浸式状态栏 保证splash到主页不会发生跳动 这样造成content顶到最上面 自带一个paddingtop，状态栏盖在上面，但是一次横竖屏切换回来，content又变为在状态栏下面
            // 所以这里只需要设置content的padding是0即可  在找到解决首页沉浸式方案之前 暂时 这么做
            rootView.setPadding(rootView.getPaddingLeft(),
                    0,
                    rootView.getPaddingRight(),
                    rootView.getPaddingBottom());
        }
        try {
            View view = window.getDecorView();
            if (show) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                } else if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                    view.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE);
                } else {
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                }
            }
        } catch (Throwable ignore) {}
    }

    public static void setTranslucentStatusBar(Window window, int colorId) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window, colorId);
        } else if (sdkInt >= VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window, int colorId) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(colorId));
    }

    @TargetApi(VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}
