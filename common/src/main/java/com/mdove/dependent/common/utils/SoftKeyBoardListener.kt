package com.mdove.dependent.common.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

/**
 * Trick listener.
 * Implementation is to observe layout height
 */
class SoftKeyBoardListener constructor(activity: Activity) {
    private val rootView: View = activity.window.decorView
    private var rootViewVisibleHeight: Int = 0
    var onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        //获取当前根视图在屏幕上显示的大小
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val visibleHeight = r.height()
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }

        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return@OnGlobalLayoutListener
        }

        //根视图显示高度变小超过200，可以看作软键盘显示了
        if (rootViewVisibleHeight - visibleHeight > 200) {
            onSoftKeyBoardChangeListener?.keyBoardShow(rootViewVisibleHeight - visibleHeight)
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }

        //根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - rootViewVisibleHeight > 200) {
            onSoftKeyBoardChangeListener?.keyBoardHide(visibleHeight - rootViewVisibleHeight)
            rootViewVisibleHeight = visibleHeight
        }
    }

    init {
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun release() {
        try {
            rootView.viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener)
        } catch (e: Exception) {
        }
    }

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)

        fun keyBoardHide(height: Int)
    }
}