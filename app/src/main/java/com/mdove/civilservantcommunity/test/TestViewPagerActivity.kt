package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test_view_pager.*

/**
 * Created by zhaojing on 2019-09-07.
 */
class TestViewPagerActivity : BaseActivity() {
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view_pager)
        adapter = ViewPagerAdapter(supportFragmentManager)
        vp.adapter = adapter

        vp.postDelayed({
            Log.d("mdove","notifyDataSetChanged")
            adapter.notifyDataSetChanged()
        }, 10000)
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return super.instantiateItem(container, position)
        }

        override fun getItemPosition(fragment: Any): Int {
            return if (fragment is TestFragment2) {
                POSITION_NONE
            } else {
                super.getItemPosition(fragment)
            }
        }

        override fun getItem(position: Int): Fragment {
            Log.d("mdove", "getItem -> $position")
            return when (position) {
                0 -> TestFragment1()
                1 -> TestFragment2()
                else -> TestFragment3()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}