package com.mdove.civilservantcommunity.detailfeed

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedResp
import kotlinx.android.synthetic.main.layout_detail_feed_head_view.view.*
import kotlin.math.abs

/**
 * Created by MDove on 2019-10-26.
 */
class DetailFeedHeadView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr){

    init {
        inflate(context, R.layout.layout_detail_feed_head_view, this)
    }

    fun updateUI(data: DetailFeedResp?) {
        data?.let {
            tv_toolbar_name.text =
                if (TextUtils.isEmpty(it.userInfo?.username)) "匿名用户" else it.userInfo?.username
            tv_toolbar_title.text = it.title ?: "分享详情"
        }
    }

    fun updateHeaderVerticalOffset(verticalOffset: Int, visibleHeaderHeight: Int) {
        val translateOffSet = abs(verticalOffset)

        val percent = if (translateOffSet > 0 && visibleHeaderHeight > 0) {
            translateOffSet.toFloat() / visibleHeaderHeight
        } else {
            view_toolbar.visibility = View.GONE
            0.0f
        }
        setTitleInfoAlpha(abs(percent))
    }

    private fun setTitleInfoAlpha(percent: Float) {
        view_toolbar.visibility = View.VISIBLE
        tv_toolbar_title.alpha = percent
        layout_nick.alpha = percent
    }
}