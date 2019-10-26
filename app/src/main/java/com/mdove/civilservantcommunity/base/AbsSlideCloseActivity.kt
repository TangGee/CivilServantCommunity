package com.mdove.civilservantcommunity.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.mdove.dependent.common.view.sliding.MDovePanelSlideListener
import com.mdove.dependent.common.view.sliding.PagerEnabledSlidingPaneLayout
import com.ss.android.buzz.base.MDoveSlideableListener

/**
 * Created by MDove on 2019-10-26.
 */
abstract class AbsSlideCloseActivity : BaseActivity(), MDovePanelSlideListener,
    MDoveSlideableListener {

    protected lateinit var mSlidingPaneLayout: PagerEnabledSlidingPaneLayout
    protected lateinit var mContentView: FrameLayout

    override var slideable
        get() = mSlidingPaneLayout.prohibitSideslip
        set(value) {
            mSlidingPaneLayout.prohibitSideslip = !(value && canSlideNow())
        }

    open fun canSlideNow(): Boolean {//便于子类重写
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSlidingPaneLayout()
    }

    private fun initSlidingPaneLayout() {
        val slidingPaneLayout = PagerEnabledSlidingPaneLayout(this)
        slidingPaneLayout.setPanelSlideListener(this)
        slidingPaneLayout.sliderFadeColor = ContextCompat.getColor(this, android.R.color.transparent)
        // mOverhangSize值为菜单到右边屏幕的最短距离，默认是32dp
        try {
            val overhangSize = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
            overhangSize.isAccessible = true
            overhangSize.set(slidingPaneLayout, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 左侧的透明视图
        val leftView = View(this)
        leftView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slidingPaneLayout.addView(leftView, 0)

        // 这种注释的代码性能更好， 因为层级更少，但是兼容性不是很好，各种状态栏处理有问题，后续再优化吧，着急发版
//            (window.decorView as ViewGroup).let { decorView ->
//                val rightView = decorView.getChildAt(0)
//                decorView.removeView(rightView)
//
//                rightView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
//                slidingPaneLayout.addView(rightView, 1)
//                decorView.addView(slidingPaneLayout)
//            }

        val contentView = FrameLayout(this)
        contentView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        contentView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        slidingPaneLayout.addView(contentView, 1)

        mSlidingPaneLayout = slidingPaneLayout
        mContentView = contentView
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(layoutInflater.inflate(layoutResID, null))
    }

    override fun setContentView(view: View?) {
        setContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(mSlidingPaneLayout, params)
        mContentView.removeAllViews()
        mContentView.addView(view)
    }

    override fun onPanelClosed(panel: View?) {
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
    }

    override fun onPanelOpened(panel: View?) {
        // 滑动的时候关闭退出动画，其他场景动画正常
        onBackPressed()
    }
}
