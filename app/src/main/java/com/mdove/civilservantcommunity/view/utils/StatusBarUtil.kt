package com.mdove.civilservantcommunity.view.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

import com.mdove.civilservantcommunity.R

import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

object StatusBarUtil {
    fun setTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 设置根布局的参数
            //            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            //            ViewGroup rootView = (ViewGroup) content.getChildAt(0);
            //            rootView.setFitsSystemWindows(true);
            //            rootView.setClipToPadding(true);

        }
    }

    fun setColor(activity: Activity, color: Int) {
        try {
            if (Build.VERSION.SDK_INT > VERSION_CODES.KITKAT) {
                // 设置状态栏透明
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                // 生成一个状态栏大小的矩形
                val statusView = createStatusView(activity, color)
                // 添加 statusView 到布局中
                val decorView = activity.window.decorView as ViewGroup
                decorView.addView(statusView)
                // 设置根布局的参数
                val rootView =
                    (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
                rootView.fitsSystemWindows = true
                rootView.clipToPadding = true
            }
        } catch (ignore: Throwable) {
        }

    }

    private fun createStatusView(activity: Activity, color: Int): View {
        val statusBarHeight = getStatausBarHeight(activity)
        // 绘制一个和状态栏一样高的矩形
        val statusView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            statusBarHeight
        )
        statusView.layoutParams = params
        statusView.setBackgroundColor(ContextCompat.getColor(activity, color))
        return statusView
    }

    // 获得状态栏高度
    fun getStatausBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun showStatusBar(activity: Activity?, show: Boolean) {
        if (activity == null) {
            return
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
            val window = activity.window
            if (show) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                val decorView = window.decorView
                val uiOptions = if (show)
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                else
                    SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                val rootView =
                    (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
                rootView?.setPadding(
                    rootView.paddingLeft,
                    0,
                    rootView.paddingRight,
                    rootView.paddingBottom
                )

                decorView.systemUiVisibility = uiOptions
            }
        } catch (ignore: Throwable) {
        }

    }

    fun showVenusStatusBar(activity: Activity, withFullFlag: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                val window = activity.window
                val hasFullScreenFlag =
                    window.attributes != null && window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
                if (hasFullScreenFlag) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                //设置状态栏颜色
                window.statusBarColor = activity.resources.getColor(R.color.white, null)
                val decorView = window.decorView
                var uiOptions = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                if (withFullFlag) {
                    uiOptions = uiOptions or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                val rootView =
                    (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
                rootView?.setPadding(
                    rootView.paddingLeft,
                    0,
                    rootView.paddingRight,
                    rootView.paddingBottom
                )
                decorView.systemUiVisibility = uiOptions
            }
        } catch (ignore: Throwable) {
        }

    }

    fun setVenusStatusBarColor(activity: Activity, @ColorInt statusColor: Int) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                val window = activity.window
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                //设置状态栏颜色
                window.statusBarColor = statusColor
                //设置状态栏图标深色
                window.decorView.systemUiVisibility =
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } catch (ignore: Throwable) {
        }

    }

    fun setVenusDetailStatusBarStyle(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                val flags =
                    SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                activity.window.decorView.systemUiVisibility = flags
                activity.window.statusBarColor = Color.TRANSPARENT

            }
        } catch (ignore: Throwable) {
        }

    }

    fun setVenusDetailImmersStatusBarStyle(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                val flags = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                activity.window.decorView.systemUiVisibility = flags

                activity.window.statusBarColor = Color.TRANSPARENT

            }
        } catch (ignore: Throwable) {
        }

    }


    fun hideStatusBar(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                activity.window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN or
                        SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        } catch (ignore: Throwable) {
        }

    }

    fun showOrHideNaviBar(activity: Activity?, show: Boolean) {
        if (activity == null) {
            return
        }
        val window = activity.window ?: return
        val rootView =
            (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView?.setPadding(
            rootView.paddingLeft,
            0,
            rootView.paddingRight,
            rootView.paddingBottom
        )
        try {
            val view = window.decorView
            if (show) {
                view.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                    view.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                } else if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                    view.systemUiVisibility =
                        SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LOW_PROFILE
                } else {
                    view.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
                }
            }
        } catch (ignore: Throwable) {
        }

    }

    fun setTranslucentStatusBar(window: Window?, colorId: Int) {
        if (window == null) return
        val sdkInt = Build.VERSION.SDK_INT
        if (sdkInt >= VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window, colorId)
        } else if (sdkInt >= VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window)
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    private fun setTranslucentStatusBarLollipop(window: Window, colorId: Int) {
        window.statusBarColor = window.context
            .resources
            .getColor(colorId)
    }

    @TargetApi(VERSION_CODES.KITKAT)
    private fun setTranslucentStatusBarKiKat(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}
