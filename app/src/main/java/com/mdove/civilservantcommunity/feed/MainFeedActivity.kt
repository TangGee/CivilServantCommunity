package com.mdove.civilservantcommunity.feed

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.account.AccountActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainFeedActivity : BaseActivity() {
    companion object {
        private const val TAG_FEED_FRAGMNET = "tag_feed_fragment"
        private const val TAG_ME_FRAGMNET = "tag_me_fragment"

        fun gotoMain(context:Context){
            val intent = Intent(context, MainFeedActivity::class.java)
            context.startActivity(intent)
            (context as? Activity)?.let{
                it.finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogin()
        setContentView(R.layout.activity_main)
        initViewPager()
        initTabLayout()

        Looper.getMainLooper().setMessageLogging {
            Looper.getMainLooper().thread.stackTrace.forEach {
                Log.d("mdove",it.methodName)
            }
        }
    }

    private fun initLogin() {
        AppConfig.getUserInfo() ?: also {
            gotoLogin()
        }
    }

    private fun gotoLogin() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initTabLayout() {
        val titles = ArrayList<String>()
        titles.add("热门")
        titles.add("我的")
        stl.setViewPager(vp, titles)
    }

    private fun initViewPager() {
        vp.adapter = ViewPagerAdapter(supportFragmentManager)
    }

    inner class ViewPagerAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            val tag = if (position == 0) TAG_FEED_FRAGMNET else TAG_ME_FRAGMNET
            return fm.findFragmentByTag(tag) ?: let {
                if (position == 0) {
                    MainFeedFragment()
                } else {
                    MePageFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }

    }
}