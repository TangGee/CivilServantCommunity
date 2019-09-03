package com.mdove.civilservantcommunity

import android.os.Bundle
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.login.RegisterFragment

/**
 * Created by MDove on 2019-09-02.
 */
class AccountActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.content, RegisterFragment(), "Haha")
                .commit()
        }
    }
}