package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mdove.civilservantcommunity.R
import kotlinx.android.synthetic.main.activity_test_leak.*

/**
 * Created by MDove on 2019-09-11.
 */
class TestLeakActivity :AppCompatActivity() {
    private val name="TestLeakActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_leak)

        bnt_one.setOnClickListener {
            Handler().postDelayed({
                val newName= "$name~One"
                Log.d("mdove",newName)
            },5000)
        }

        bnt_two.setOnClickListener {
            Handler().postDelayed({
                val newName= "$name~Two"
                Log.d("mdove",newName)
            },15000)
        }
    }
}