package com.mdove.civilservantcommunity.feed

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.AccountActivity
import com.mdove.civilservantcommunity.account.bean.UserInfoParams
import com.mdove.civilservantcommunity.account.bean.toUserInfoParams
import com.mdove.civilservantcommunity.account.gotoUpdateUserInfo
import com.mdove.civilservantcommunity.account.utils.IdentitysHelper
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.detailfeed.DetailFeedActivity
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.feed.adapter.MePageAdapter
import com.mdove.civilservantcommunity.feed.adapter.OnMePageClickListener
import com.mdove.civilservantcommunity.account.bean.MePageAnswerInfoMePage
import com.mdove.civilservantcommunity.account.bean.MePageArticleInfoMePage
import com.mdove.civilservantcommunity.account.bean.MePageQuestionInfoMePage
import com.mdove.civilservantcommunity.feed.viewmodel.MePageViewModel
import com.mdove.civilservantcommunity.ugc.MainUGCActivity
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.threadpool.FastMain
import kotlinx.android.synthetic.main.fragment_me_page.*
import kotlinx.coroutines.launch

/**
 * Created by MDove on 2019-09-07.
 */
class MePageFragment : BaseFragment() {
    private lateinit var viewModel: MePageViewModel
    private lateinit var mAdapter: MePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MePageViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_me_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_update.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        view_toolbar.setTitle("我的主页")
        mAdapter = MePageAdapter(object : OnMePageClickListener {
            override fun onClickArticle(article: MePageArticleInfoMePage) {
                if (context != null && article.aid != null) {
                    DetailFeedActivity.gotoFeedDetail(context!!, DetailFeedParams(article.aid))
                }
            }

            override fun onClickQuestion(article: MePageQuestionInfoMePage) {
            }

            override fun onClickAnswer(article: MePageAnswerInfoMePage) {
            }
        })
        rlv.layoutManager = LinearLayoutManager(context)
        rlv.adapter = mAdapter
        viewModel.data.observe(this, Observer { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    res.data?.data?.let {
                        srf.isRefreshing = false
                        viewModel.paramsLiveData.value = it.toUserInfoParams()
                    }
                }
                Status.LOADING -> {
                    srf.isRefreshing = true
                }
                Status.ERROR -> {
                    srf.isRefreshing = false
                }
            }
        })

        viewModel.paramsLiveData.observe(this, Observer {
            refreshUI(it)
        })

        srf.setOnRefreshListener {
            AppConfig.getUserInfo()?.let {
                viewModel.reqMePage(it.uid)
            }
        }

        btn_update.setOnClickListener {
            context?.let { context ->
                (activity as? ActivityLauncher)?.let { launcher ->
                    launch(FastMain) {
                        viewModel.paramsLiveData.value?.let {
                            launcher.gotoUpdateUserInfo(context, it).params?.let { resultParams ->
                                viewModel.paramsLiveData.value = resultParams
                            }
                        }
                    }
                }
            }
        }

        btn_logout.setOnClickListener {
            AppConfig.setUserInfo(null)
            context?.let {
                AccountActivity.gotoAccount(it)
                activity?.finish()
            }
        }

        AppConfig.getUserInfo()?.let {
            viewModel.reqMePage(it.uid)
        }

        layout_empty_add.setOnClickListener {
            context?.let {
                MainUGCActivity.gotoMainUGC(it)
            }
        }

        layout_edit.setOnClickListener {
            context?.let {
                MainUGCActivity.gotoMainUGC(it)
            }
        }
    }

    private fun refreshUI(params: UserInfoParams) {
        AppConfig.setUserInfo(UserInfo(params.uid, params.userName))
        tv_name.text = params.userName
        tv_type.text = IdentitysHelper.getIdentity(params.userType)
        if (params.feedArticleList != null) {
            layout_empty.visibility = View.GONE
            layout_edit.visibility = View.VISIBLE
            mAdapter.submitList(params.feedArticleList)
        } else {
            layout_edit.visibility = View.GONE
            layout_empty.visibility = View.VISIBLE
        }
    }
}