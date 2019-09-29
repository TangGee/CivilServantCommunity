package com.mdove.civilservantcommunity.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.UpdateUserInfoActivity.Companion.INTENT_PARAMS
import com.mdove.civilservantcommunity.account.bean.UserInfoParams
import com.mdove.civilservantcommunity.account.fragment.UpdateUserInfoFragment
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher

/**
 * Created by MDove on 2019-09-16.
 */
class UpdateUserInfoActivity : BaseActivity() {
    companion object {
        const val TAG_UPDATE_USER_INFO_FRAGMENT = "tag_update_user_info_fragment"
        const val INTENT_PARAMS = "intent_params_update_user_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                R.id.content,
                UpdateUserInfoFragment.newInstance(intent.getParcelableExtra(INTENT_PARAMS)),
                TAG_UPDATE_USER_INFO_FRAGMENT
            ).commit()
        }
    }
}

suspend fun ActivityLauncher.gotoUpdateUserInfo(
    context: Context,
    params: UserInfoParams
): UpdateInfoResult {
    val intent = Intent(context, UpdateUserInfoActivity::class.java)
    intent.putExtra(INTENT_PARAMS, params)
    return startActivityAsync(intent).await().run {
        return if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                UpdateInfoResult(data.getParcelableExtra(INTENT_PARAMS), Status.SUC)
            } else {
                UpdateInfoResult(null, Status.CANCEL)
            }
        } else {
            UpdateInfoResult(null, Status.CANCEL)
        }
    }
}