package com.mdove.civilservantcommunity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.viewpager.widget.ViewPager
import com.mdove.civilservantcommunity.base.BaseActivity
import com.mdove.civilservantcommunity.feed.MainFeedFragment
import com.mdove.civilservantcommunity.feed.MeFragment
import com.mdove.dependent.common.view.tablayout.calculateGradualColor
import com.mdove.dependent.common.view.tablayout.gradualColor
import com.mdove.dependent.common.view.tablayout.scrollNextPosition
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    companion object {
        private const val TAG_FEED_FRAGMNET = "tag_feed_fragment"
        private const val TAG_ME_FRAGMNET = "tag_me_fragment"
    }

    val evaluator = ArgbEvaluator()
    var selectedColor: Int = 0
    var unSelectedColor: Int = 0
    private val indicatorColors = arrayListOf<Int>(
        Color.parseColor("#FF5E00"),
        Color.parseColor("#FF8000"), Color.parseColor("#FFA200"), Color.parseColor("#FFBC34")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager()
        initTabLayout()
    }

    private fun initTabLayout() {
        var titles = ArrayList<String>()
        val colors = mutableListOf<Int>()
        vp?.adapter?.let { it_ ->
            (0 until it_.count).forEach { index ->
                titles.add(if (index == 0) "热门" else "我的")
                colors.add(indicatorColors[index % indicatorColors.size])
            }
        }
        stl.setViewPager(vp, titles)
    }

    private fun initViewPager() {
        selectedColor = ContextCompat.getColor(this, R.color.amber_500)
        unSelectedColor = ContextCompat.getColor(this, R.color.amber_200)
        vp.adapter = ViewPagerAdapter(supportFragmentManager)
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var pageSelected = false

            override fun onPageScrollStateChanged(state: Int) {
                if (0 == state) pageSelected = false
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset > 0.0f && !pageSelected) {//略微复杂一点，并且0要排除，走tab select时机
                    stl.gradualColor(
                        vp.currentItem, vp.scrollNextPosition(position),
                        evaluator.calculateGradualColor(
                            positionOffset,
                            selectedColor,
                            unSelectedColor
                        ),
                        evaluator.calculateGradualColor(
                            positionOffset,
                            unSelectedColor,
                            selectedColor
                        )
                    )
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    inner class ViewPagerAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            val tag = if (position == 0) TAG_FEED_FRAGMNET else TAG_ME_FRAGMNET
            return fm.findFragmentByTag(tag) ?: let {
                if (position == 0) {
                    MainFeedFragment()
                } else {
                    MeFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }

    }
}
