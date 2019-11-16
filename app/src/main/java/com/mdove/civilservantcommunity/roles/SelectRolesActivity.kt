package com.mdove.civilservantcommunity.roles

import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity

/**
 * Created by MDove on 2019-11-16.
 */
class SelectRolesActivity : BaseActivity() {
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