package com.mdove.civilservantcommunity.base.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Deferred

/**
 * Created by MDove on 2019-09-29.
 */
abstract class BaseLauncherActivity :AppCompatActivity(),ActivityLauncher {
    private val mLauncherHelper = ActivityLauncher.Helper()

    override val context: Context?
        get() = this

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mLauncherHelper.onCreate(this)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        super<ActivityLauncher>.startActivity(intent, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mLauncherHelper.handleActivityResult(this, requestCode, resultCode, data)
    }

    override fun startActivityAsync(
        intent: Intent,
        options: Bundle?
    ): Deferred<ActivityLauncher.Result> {
        return mLauncherHelper.startActivityForResult(this, intent, options)
    }

    override fun addResultHandler(handler: ActivityLauncher.IResultHandler) {
        mLauncherHelper.addResultHandler(handler)
    }

    override fun removeResultHandler(handler: ActivityLauncher.IResultHandler) {
        mLauncherHelper.removeResultHandler(handler)
    }
}