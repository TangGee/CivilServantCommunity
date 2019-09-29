package com.mdove.civilservantcommunity.base.launcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Created by MDove on 2019-09-29.
 */
interface ActivityLauncher : LifecycleOwner{

    val context: Context?

    fun startActivity(intent: Intent, options: Bundle? = null) {
        startActivityAsync(intent, options).start()
    }

    fun startActivityAsync(intent: Intent, options: Bundle? = null): Deferred<Result>

    /**
     * Warning: 添加了Handler之后要及时移出
     */
    fun addResultHandler(handler: IResultHandler)

    fun removeResultHandler(handler: IResultHandler)

    interface IResultHandler {
        fun onActResult(
            launcher: ActivityLauncher,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        )
    }

    data class Result(val resultCode: Int, val data: Intent?)

    class Helper:LifecycleObserver  {

        companion object {
            private const val REQUEST_CODE = 31966
        }

        private val resultHandlerSet = mutableSetOf<IResultHandler>()
        private var deferredResult: CompletableDeferred<Result>? = null
        private lateinit var mLifecycleOwner: LifecycleOwner

        fun onCreate(lifecycleOwner: LifecycleOwner) {
            mLifecycleOwner=lifecycleOwner
            mLifecycleOwner.lifecycle.addObserver(this)
        }

        fun startActivityForResult(activity: Activity, intent: Intent, options: Bundle?): Deferred<Result> {
            deferredResult = null
            ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE, options)
            val deferred = CompletableDeferred<Result>()
            deferredResult = deferred
            return deferred
        }

        fun startActivityForResult(fragment: Fragment, intent: Intent, options: Bundle?): Deferred<Result> {
            deferredResult = null
            fragment.startActivityForResult(intent, REQUEST_CODE, options)
            val deferred = CompletableDeferred<Result>()
            deferredResult = deferred
            return deferred
        }

        fun addResultHandler(handler: IResultHandler) = resultHandlerSet.add(handler)

        fun removeResultHandler(handler: IResultHandler) = resultHandlerSet.remove(handler)

        fun handleActivityResult(
            launcher: ActivityLauncher,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            if (requestCode == REQUEST_CODE && deferredResult != null) {
                val deferred = deferredResult ?: return
                deferredResult = null
                deferred.complete(Result(resultCode, data))
            }

            resultHandlerSet.toList().map {
                it.onActResult(launcher, requestCode, resultCode, data)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestory() {
            mLifecycleOwner.lifecycle.removeObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            deferredResult = null
        }
    }
}
