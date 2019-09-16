@file:JvmName("UIUtility")
package com.mdove.dependent.common.utils

import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mdove.dependent.common.loading.LoadingDialogFragment
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.view.DebounceOnClickListener
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Created by MDove on 2019-09-16.
 */
fun ImageView.setImageFile(file: File): Boolean {
    return try {
        setImageURI(Uri.fromFile(file))
        true
    } catch (ignore: Throwable) {
        false
    }
}

fun View.setDebounceOnClickListener(action: (View) -> Unit) {
    setDebounceOnClickListener(DebounceOnClickListener.DEFAULT_DELAY, action)
}

fun View.setDebounceOnClickListener(delay: Long, action: (View) -> Unit) {
    this.setOnClickListener(object : DebounceOnClickListener(delay) {
        override fun doClick(var1: View?) {
            if (var1 != null) {
                action(var1)
            }
        }
    })
}

fun View.layoutIdTrace(resource: Resources): String {
    var current: View? = this
    val result = StringBuilder()
    while(current != null) {
        try {
            result.append(resource.getResourceName(current.id))
        } catch (e: Exception) {
            break
        }
        result.append(" <- ")
        current = current.parent as? View?
    }
    return result.toString()
}

@MainThread
fun FragmentActivity.showLoading(tips:String? = null) {
    if (!supportFragmentManager.isStateSaved
        && !supportFragmentManager.isDestroyed
        && supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) == null) {
        LoadingDialogFragment.newInstance(tips).showNow(supportFragmentManager,LoadingDialogFragment.TAG)
    }
}

@MainThread
fun FragmentActivity.dismissLoading() {
    (supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) as? LoadingDialogFragment)?.dismissAllowingStateLoss()
}

@MainThread
fun Fragment.showLoading(tips: String? = null) {
    if (isAdded
        && !childFragmentManager.isStateSaved
        && !childFragmentManager.isDestroyed
        && childFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) == null) {
        LoadingDialogFragment.newInstance(tips).showNow(childFragmentManager, LoadingDialogFragment.TAG)
    }
}

@MainThread
fun Fragment.dismissLoading() {
    if (host == null) {
        return
    }
    (childFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) as? LoadingDialogFragment)?.dismissAllowingStateLoss()
}

@MainThread
suspend fun FragmentActivity.safeShowLoading(tips:String? = null) {
    withContext(FastMain) {
        if (!supportFragmentManager.isStateSaved
            && !supportFragmentManager.isDestroyed
            && supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) == null
        ) {
            LoadingDialogFragment.newInstance(tips)
                .showNow(supportFragmentManager, LoadingDialogFragment.TAG)
        }
    }
}

@MainThread
suspend fun FragmentActivity.safeDismissLoading() {
    withContext(FastMain) {
        (supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) as? LoadingDialogFragment)?.dismissAllowingStateLoss()
    }
}

@MainThread
suspend fun Fragment.safeShowLoading(tips:String? = null) {
    withContext(FastMain) {
        if (!childFragmentManager.isStateSaved
            && !childFragmentManager.isDestroyed
            && isAdded
            && childFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) == null
        ) {
            LoadingDialogFragment.newInstance(tips)
                .showNow(childFragmentManager, LoadingDialogFragment.TAG)
        }
    }
}

@MainThread
suspend fun Fragment.safeDismissLoading() {
    withContext(FastMain) {
        if (this@safeDismissLoading.host == null) {
            return@withContext
        }
        (childFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG) as? LoadingDialogFragment)?.dismissAllowingStateLoss()
    }
}
