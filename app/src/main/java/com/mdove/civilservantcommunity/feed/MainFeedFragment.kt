package com.mdove.civilservantcommunity.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.detailfeed.DetailFeedActivity
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter
import com.mdove.civilservantcommunity.feed.adapter.OnMainFeedClickListener
import com.mdove.civilservantcommunity.feed.adapter.OnMainFeedTodayPlanCheckListener
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansRespWrapper
import com.mdove.civilservantcommunity.feed.bean.FeedTodayPlansCheckParams
import com.mdove.civilservantcommunity.feed.viewmodel.MainFeedViewModel
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.gotoPlanActivity
import com.mdove.civilservantcommunity.punch.bean.PunchReq
import com.mdove.civilservantcommunity.punch.viewmodel.PunchViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.civilservantcommunity.ugc.MainUGCActivity
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_main_feed.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedFragment : BaseFragment() {
    private lateinit var feedViewModel: MainFeedViewModel
    private lateinit var punchViewModel: PunchViewModel
    private val adapter = MainFeedAdapter(object : OnMainFeedClickListener {
        override fun onClick(type: Int, resp: ArticleResp?) {
            when (type) {
                MainFeedAdapter.TYPE_FEED_PUNCH -> {
                    clickPunch()
                }
                MainFeedAdapter.TYPE_FEED_UGC -> {
                    clickUGC()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_UGC -> {
                    clickUGC()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_PUNCH -> {
                    clickPunch()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_PLAN -> {
                    clickPlan()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_ME -> {
                    clickMePage()
                }
                MainFeedAdapter.TYPE_FEED_PLAN -> {
                    clickPlan()
                }
                else -> {
                    resp?.aid?.let { aid ->
                        context?.let { context ->
                            DetailFeedActivity.gotoFeedDetail(context, DetailFeedParams(aid))
                        }
                    }
                }
            }
        }
    }, object : OnMainFeedTodayPlanCheckListener {
        override fun onCheck(wrapper: FeedTimeLineFeedTodayPlansRespWrapper, isCheck: Boolean) {
            launch {
                showLoading()
                withContext(MDoveBackgroundPool) {
                    MainDb.db.todayPlansDao().update(
                        TodayPlansEntity(
                            wrapper.entityId,
                            date = wrapper.date,
                            createDate = wrapper.createTime,
                            sucDate = System.currentTimeMillis(),
                            resp = wrapper.resp.copy(
                                select = isCheck
                            )
                        )
                    )
                }
                dismissLoading()
                feedViewModel.checkTodayPlanLiveData.value =
                    FeedTodayPlansCheckParams(wrapper, isCheck)
            }
        }
    })

    private fun clickMePage() {
        context?.let {
            MePageActivity.gotoMePage(it)
        }
    }

    private fun clickPunch() {
        AppConfig.getUserInfo()?.let {
            showLoading("打卡中...")
            punchViewModel.punch(PunchReq(it.uid, System.currentTimeMillis()))
        }
    }

    private fun clickUGC() {
        context?.let {
            MainUGCActivity.gotoMainUGC(it)
        }
    }

    private fun clickPlan() {
        launch(FastMain) {
            showLoading()
            withContext(MDoveBackgroundPool) {
                MainDb.db.todayPlansDao().getTodayPlansRecord()?.isNotEmpty() ?: false
            }.takeIf {
                it
            }?.let {
                dismissLoading()
                ToastUtil.toast("今天的计划早已生成~", Toast.LENGTH_SHORT)
            } ?: also {
                (activity as? ActivityLauncher)?.let {
                    dismissLoading()
                    it.gotoPlanActivity(context!!).params?.let {
                        feedViewModel.planParamsLiveData.value = it
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            feedViewModel = ViewModelProviders.of(it).get(MainFeedViewModel::class.java)
            punchViewModel = ViewModelProviders.of(it).get(PunchViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlv.adapter = adapter
        rlv.layoutManager = LinearLayoutManager(context)
        feedViewModel.mData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        adapter.submitList(list)
                        sfl.isRefreshing = false
                        rlv.updateEmptyUI()
                    }
                }
                Status.LOADING -> {
                    sfl.isRefreshing = true
                    rlv.updateEmptyUI()
                }
                Status.ERROR -> {
                    sfl.isRefreshing = false
                    ToastUtil.toast("请求失败", Toast.LENGTH_SHORT)
                }
            }
        })

        punchViewModel.punchResp.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.status == 0) {
                        feedViewModel.punchResp.value = true
                    }
                    ToastUtil.toast("${it.data?.message}", Toast.LENGTH_SHORT)
                    dismissLoading()
                }
                Status.ERROR -> {
                    feedViewModel.punchResp.value = false
                    dismissLoading()
                }
            }
        })

        sfl.setOnRefreshListener {
            feedViewModel.reqFeed()
        }

        feedViewModel.reqFeed()

    }
}