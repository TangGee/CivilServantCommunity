package com.mdove.civilservantcommunity.base.fragment

import android.content.DialogInterface
import android.view.ViewConfiguration
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Created by MDove on 2019-11-10.
 */
abstract class SafeShowDialogFragment : DialogFragment() {

    private var lastTryShowTime: Long = 0L

    /**
     * 修复原生的bug
     * @param manager
     * @param tag
     */
    override fun show(manager: FragmentManager?, tag: String?) {
        //debounce
        val curTime = System.currentTimeMillis()
        var skip = false
        if ((curTime - lastTryShowTime) < ViewConfiguration.getDoubleTapTimeout()) {
            skip = true
        }
        lastTryShowTime = curTime
        if (skip) {
            return
        } else {
            //反射私有属性
            val clazz = javaClass.superclass as Class<*>
            try {
                val mDismissed = clazz.getDeclaredField("mDismissed")
                mDismissed.isAccessible = true
                mDismissed.set(this, false)
            } catch (e: Exception) {
            }

            try {
                val mShownByMe = clazz.getDeclaredField("mShownByMe")
                mShownByMe.isAccessible = true
                mShownByMe.set(this, true)
            } catch (e: Exception) {
            }

            showCatchException(manager, tag)
        }
    }

    private fun showCatchException(manager: FragmentManager?, tag: String?) {
        if (!isAdded) {
            try {
                val ft = manager?.beginTransaction()
                ft?.add(this, tag ?: this::class.java.simpleName)
                ft?.commitAllowingStateLoss()
            } catch (e: Exception) {
//                e.printStackTrace()
            }

        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        dismissAllowingStateLoss()
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override fun dismissAllowingStateLoss() {
        if (fragmentManager != null && isAdded) {
            super.dismissAllowingStateLoss()
        }
    }
}