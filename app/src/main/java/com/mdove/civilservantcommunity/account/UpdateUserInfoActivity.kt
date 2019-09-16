package com.mdove.civilservantcommunity.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.fragment.UpdateUserInfoFragment
import com.mdove.civilservantcommunity.base.BaseActivity

/**
 * Created by MDove on 2019-09-16.
 */
class UpdateUserInfoActivity :BaseActivity() {
    companion object {
        const val TAG_UPDATE_USER_INFO_FRAGMENT = "tag_update_user_info_fragment"

        fun gotoUpdateUserInfo(context: Context) {
            val intent = Intent(context, UpdateUserInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().add(R.id.content, UpdateUserInfoFragment(),TAG_UPDATE_USER_INFO_FRAGMENT).commit()
        }
    }
}