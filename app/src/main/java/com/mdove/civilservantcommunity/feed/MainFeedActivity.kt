package com.mdove.civilservantcommunity.feed

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mdove.apt_annotation.TestAnnotation
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.AccountActivity
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.roles.SelectRolesActivity
import com.mdove.civilservantcommunity.roles.gotoSelectRolesActivity
import com.mdove.dependent.common.toast.ToastUtil
import kotlinx.coroutines.launch

class MainFeedActivity : BaseActivity() {
    companion object {
        private const val TAG_FEED_FRAGMNET = "tag_feed_fragment"
        private const val TAG_ME_FRAGMNET = "tag_me_fragment"

        fun gotoMain(context: Context) {
            val intent = Intent(context, MainFeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    @TestAnnotation(123)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSelectRoles()
        setContentView(R.layout.activity_feed_main)
//        initViewPager()
//        initTabLayout()

//        Looper.getMainLooper().setMessageLogging {
//            Looper.getMainLooper().thread.stackTrace.forEach {
//                Log.d("mdove", it.methodName)
//            }
//        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFeedFragment(), TAG_FEED_FRAGMNET)
                .commit()
        }
    }

    private fun initSelectRoles() {
        launch {
            gotoSelectRolesActivity(context!!).params?.let {
                ToastUtil.toast("你选择了：${it.title}")
            }
        }
//        AppConfig.hasSelectRoles().takeIf {
//            it
//        }?.let {
//            launch {
//                gotoSelectRolesActivity(context!!).params?.let {
//                    ToastUtil.toast("你选择了：${it.title}")
//                }
//            }
//        }
    }

    private fun initLogin() {
        AppConfig.getUserInfo() ?: also {
            gotoLogin()
        }
    }

    private fun gotoLogin() {
        AccountActivity.gotoAccount(this)
        finish()
    }

    private fun initTabLayout() {
//        val titles = ArrayList<String>()
//        titles.add("热门")
//        titles.add("我的")
//        stl.setViewPager(vp, titles)
    }

    private fun initViewPager() {
//        vp.adapter = ViewPagerAdapter(supportFragmentManager)
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
