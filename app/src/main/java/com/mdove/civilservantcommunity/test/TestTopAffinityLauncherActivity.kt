package com.mdove.civilservantcommunity.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mdove.civilservantcommunity.R

/**
 * Created by MDove on 2019-11-22.
 */
class TestTopAffinityLauncherActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_taskaffinity_launcher)
        findViewById<TextView>(R.id.btn_normal).setOnClickListener {
            val intent = Intent(this,TestMainLauncherActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.btn_single_task).setOnClickListener {
            val intent = Intent(this,TestSingleTaskLauncherActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("mdove","TestTopAffinityLauncherActivity - onNewIntent")
    }
}