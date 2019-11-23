package com.mdove.civilservantcommunity.test

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mdove.civilservantcommunity.R

/**
 * Created by MDove on 2019-11-22.
 */
class TestSingleTaskAffinityLauncherActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_singletask_launcher)
        findViewById<TextView>(R.id.btn_normal).setOnClickListener {
            val intent = Intent(this,TestMainLauncherActivity::class.java)
            startActivity(intent)
        }
    }
}