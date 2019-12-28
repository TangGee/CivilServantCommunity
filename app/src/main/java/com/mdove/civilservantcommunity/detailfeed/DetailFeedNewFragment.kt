package com.mdove.civilservantcommunity.detailfeed

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedResp
import com.mdove.civilservantcommunity.detailfeed.viewmodel.DetailFeedViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_detail_feed_new.*
import kotlinx.android.synthetic.main.layout_detail_feed_head_view.*

/**
 * Created by MDove on 2019-09-09.
 */
class DetailFeedNewFragment : BaseFragment() {
    private lateinit var params: DetailFeedParams
    private lateinit var viewModel: DetailFeedViewModel

    companion object {
        const val DETAIL_FEED_PARAM = "detail_feed_param"
        fun newInstance(params: DetailFeedParams): Fragment =
            DetailFeedNewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DETAIL_FEED_PARAM, params)
                    classLoader = params::class.java.classLoader
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<DetailFeedParams>(DETAIL_FEED_PARAM)?.let {
            params = it
        } ?: activity?.finish()

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(DetailFeedViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_feed_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    srl.isRefreshing = false
                    updateUI(it.data?.data)
                    dismissLoading()
                }
                Status.LOADING -> {
                    showLoading()
                    srl.isRefreshing = true
                }
                Status.ERROR -> {
                    dismissLoading()
                    srl.isRefreshing = false
                }
            }
        })

        srl.setOnRefreshListener {
            viewModel.reqDetailFeed(params.aid)
        }

        viewModel.reqDetailFeed(params.aid)
        app_bar_layout.addOnOffsetChangedListener(OnOffsetChangedListener())
    }

    private fun updateUI(data: DetailFeedResp?) {
        data?.let {
            head_view.updateUI(it)
            tv_name.text =
                if (TextUtils.isEmpty(it.userInfo?.username)) "匿名用户" else it.userInfo?.username
            tv_type.text = "暂无字段"
            tv_title.text = it.title ?: ""
            tv_content.text = it.content
            it.maketime?.let {
                tv_time.text = it
            }
        }
    }

    inner class OnOffsetChangedListener : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            if (!srl.isRefreshing) {
                srl.isEnabled = verticalOffset >= 0
            }
            val visibleHeaderHeight =
                app_bar_layout.height - if (head_view.visibility == View.VISIBLE) {
                    head_view.height
                } else {
                    0
                } - head_view.height
            head_view.updateHeaderVerticalOffset(verticalOffset, visibleHeaderHeight)
        }
    }
}