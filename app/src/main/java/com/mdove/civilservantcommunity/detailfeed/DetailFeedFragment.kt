package com.mdove.civilservantcommunity.detailfeed

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedResp
import com.mdove.civilservantcommunity.detailfeed.viewmodel.DetailFeedViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_detail_feed.*

/**
 * Created by MDove on 2019-09-09.
 */
class DetailFeedFragment : BaseFragment() {
    private lateinit var params: DetailFeedParams
    private lateinit var viewModel: DetailFeedViewModel

    companion object {
        const val DETAIL_FEED_PARAM = "detail_feed_param"
        fun newInstance(params: DetailFeedParams): Fragment =
            DetailFeedFragment().apply {
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
        return inflater.inflate(R.layout.fragment_detail_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setColorForAll(Color.WHITE)
        view_toolbar.setTitle("大神分享")

        viewModel.mData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    it.data?.data?.let{
                        updateUI(it)
                    }
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })
        viewModel.reqDetailFeed(params.aid)
    }

    private fun updateUI(data: DetailFeedResp) {
        tv_name.text =
            if (TextUtils.isEmpty(data.userInfo?.username)) "匿名用户" else data.userInfo?.username
        tv_type.text = "匿名身份"
        tv_title_.text = "标题：${data.title ?: "无标题"}"
        tv_content.text = data.content
        data.maketime?.let {
            tv_time.text = TimeUtils.getDateChinese(it)
        }
    }
}