package com.mdove.civilservantcommunity.test

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mdove.civilservantcommunity.R

/**
 * Created by MDove on 2019-11-22.
 */
class TestMainLauncherActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_one_launcher)
        findViewById<TextView>(R.id.btn_single_instance).setOnClickListener {
            val intent = Intent(this,TestSingleInstanceLauncherActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.btn_single_top).setOnClickListener {
            val intent = Intent(this,TestTopAffinityLauncherActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.btn_single_instance).setOnClickListener {
            val intent = Intent(this,TestSingleInstanceLauncherActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.btn_single_top_only).setOnClickListener {
            val intent = Intent(this,TestTopAffinityLauncherActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.btn_single_task).setOnClickListener {
            val intent = Intent(this,TestSingleTaskAffinityLauncherActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.btn_single_task_only).setOnClickListener {
            val intent = Intent(this,TestSingleTaskLauncherActivity::class.java)
            startActivity(intent)
        }
    }
}