package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_test_view_pager.*

/**
 * Created by zhaojing on 2019-09-07.
 */
class TestViewPagerActivity : BaseActivity() {
    private lateinit var adapter: ViewPagerAdapter
    private val fragmentData = mutableListOf<FragmentParams>().apply {
        add(FragmentParams("页面-1"))
        add(FragmentParams("页面-2"))
        add(FragmentParams("页面-3"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view_pager)
        adapter = ViewPagerAdapter(fragmentData, supportFragmentManager)
        vp.adapter = adapter

        vp.postDelayed({
            refreshUI()
        }, 10000)
    }

    fun refreshUI(){
        fragmentData[1].title="新的页面-2"
        adapter.notifyDataSetChanged()
    }

    inner class ViewPagerAdapter(val data: List<FragmentParams>, fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItemPosition(`object`: Any): Int {
            if(`object` is TestFragment2){
                return PagerAdapter.POSITION_NONE
            }
            return super.getItemPosition(`object`)
        }
        override fun getItem(position: Int): Fragment {
            Log.d("mdove", "getItem -> $position")
            return when (position) {
                0 -> TestFragment1.newInstance(data[position])
                1 -> TestFragment2.newInstance(data[position])
                else -> TestFragment3.newInstance(data[position])
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}

@Parcelize
data class FragmentParams(var title: String) : Parcelable