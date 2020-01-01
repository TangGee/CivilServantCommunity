package com.mdove.civilservantcommunity.roles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.roles.SelectRolesActivity.Companion.INTENT_PARAMS

/**
 * Created by MDove on 2019-11-16.
 */
class SelectRolesActivity : BaseActivity() {
    companion object {
        const val INTENT_PARAMS = "intent_params_plan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SelectRolesFragment())
                .commit()
        }
    }
}

suspend fun ActivityLauncher.gotoSelectRolesActivity(
    context: Context
): SelectRolesResult {
    val intent = Intent(context, SelectRolesActivity::class.java)
    return startActivityAsync(intent).await().run {
        return if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                SelectRolesResult(
                    this.data.getParcelableExtra(
                        INTENT_PARAMS
                    ), Status.SUC
                )
            } else {
                SelectRolesResult(
                    null,
                    Status.CANCEL
                )
            }
        } else {
            SelectRolesResult(
                null,
                Status.CANCEL
            )
        }
    }
}