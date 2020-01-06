package com.mdove.civilservantcommunity.setting.hide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity

/**
 * Created by MDove on 2020-01-06.
 */
class HideRecordActivity :AbsSlideCloseActivity() {
    companion object {
        fun goto(context: Context) {
            val intent = Intent(context, HideRecordActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hide_record)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, HideRecordFragment())
                .commit()
        }
    }
}