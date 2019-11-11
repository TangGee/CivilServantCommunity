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
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.detailfeed.DetailFeedActivity
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter
import com.mdove.civilservantcommunity.feed.adapter.OnMainFeedClickListener
import com.mdove.civilservantcommunity.feed.adapter.OnMainFeedTodayPlanCheckListener
import com.mdove.civilservantcommunity.feed.adapter.OnNormalFeedListener
import com.mdove.civilservantcommunity.feed.bean.ArticleResp
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.feed.bean.FeedTodayPlansCheckParams
import com.mdove.civilservantcommunity.feed.viewmodel.MainFeedViewModel
import com.mdove.civilservantcommunity.plan.*
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDbBean
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.model.TimeScheduleStatus
import com.mdove.civilservantcommunity.punch.bean.PunchReq
import com.mdove.civilservantcommunity.punch.viewmodel.PunchViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.civilservantcommunity.ugc.MainUGCActivity
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_main_feed.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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
                MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS -> {
                    clickPlan()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_ME -> {
                    clickMePage()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_TIME_SCHEDULE -> {
                    clickTimeSchedule()
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
    }, object : OnNormalFeedListener {
        override fun onSendNewPlanClick(content: String) {
            var single: SinglePlanBeanWrapper? = null
            launch {
                showLoading()
                withContext(MDoveBackgroundPool) {
                    // TODO 先查再更新（插入），可优化
                    MainDb.db.todayPlansDao().getTodayPlansRecord()?.let { entity ->
                        val cusModuleId = "Z6566"
                        val cusModuleName = "我的计划"
                        val cusWrapper = SinglePlanBeanWrapper(
                            SinglePlanBean(
                                AppConfig.getUserInfo()?.uid ?: UUID.randomUUID().toString()
                                , cusModuleId, cusModuleName, "1", "1", content
                            ), SinglePlanType.CUSTOM_PLAN, SinglePlanStatus.NORMAL, null
                        )
                        val copyEntity = entity.resp.params.find {
                            it.moduleId == cusModuleId
                        }?.let { findBean ->
                            // 插入到老的自定义Module
                            TodayPlansEntity(
                                entity.id,
                                entity.date,
                                entity.createDate,
                                entity.sucDate,
                                TodayPlansDbBean(
                                    entity.resp.params.map {
                                        if (it.moduleId == findBean.moduleId) {
                                            it.copy(beanSingles = it.beanSingles.toMutableList().apply {
                                                add(cusWrapper)
                                            })
                                        } else {
                                            it
                                        }
                                    }
                                )
                            )
                        } ?: let {
                            // 新建一个自定的Module
                            TodayPlansEntity(
                                entity.id,
                                entity.date,
                                entity.createDate,
                                entity.sucDate,
                                TodayPlansDbBean(
                                    entity.resp.params.toMutableList().apply {
                                        add(
                                            PlanModuleBean(
                                                cusModuleId, cusModuleName,
                                                mutableListOf(cusWrapper)
                                            )
                                        )
                                    })
                            )
                        }
                        MainDb.db.todayPlansDao().update(copyEntity)
                        withContext(FastMain) {
                            feedViewModel.editNewPlanToFeedLiveData.value =
                                FeedTimeLineFeedTodayPlansResp(
                                    entity.id,
                                    entity.date,
                                    null,
                                    entity.createDate ?: TimeUtils.getDateFromSQL(),
                                    cusWrapper
                                )
                        }
                    }
                }
                dismissLoading()
            }
        }
    }, object : OnMainFeedTodayPlanCheckListener {
        override fun onCheck(resp: FeedTimeLineFeedTodayPlansResp, isCheck: Boolean) {
            launch {
                showLoading()
                withContext(MDoveBackgroundPool) {
                    val selectSinglePlan = resp.params.beanSingle
                    MainDb.db.todayPlansDao().getFeedTodayPlan(resp.entityId)?.let {
                        // TODO 先查再更新，可优化
                        MainDb.db.todayPlansDao().update(
                            it.apply {
                                sucDate = System.currentTimeMillis()
                                this.resp = TodayPlansDbBean(params = this.resp.params.map {
                                    it.copy(beanSingles = it.beanSingles.map {
                                        if (it.beanSingle.moduleId == selectSinglePlan.moduleId && selectSinglePlan.content == it.beanSingle.content) {
                                            it.copy(statusSingle = if (isCheck) SinglePlanStatus.SELECT else SinglePlanStatus.NORMAL)
                                        } else {
                                            it
                                        }
                                    })
                                })
                            }
                        )
                    }
                }
                dismissLoading()
                feedViewModel.checkTodayPlanLiveData.value =
                    FeedTodayPlansCheckParams(resp, isCheck)
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

    private fun clickTimeSchedule() {
        (activity as? ActivityLauncher)?.let {
            launch {
                it.gotoTimeScheduleActivity(context!!, feedViewModel.createTimeScheduleParams())
                    .let {
                        if (it.timeScheduleStatus == TimeScheduleStatus.SUC) {
                            it.data?.let {
                                feedViewModel.timeScheduleToFeedLiveData.value = it
                            }
                        }
                    }
            }
        }
    }

    private fun clickPlan() {
        (activity as? ActivityLauncher)?.let {
            launch(FastMain) {
                it.gotoPlanActivity(context!!).params?.let {
                    feedViewModel.planParamsLiveData.value = it
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
                    if (it.data.isNullOrEmpty()) {
                        sfl.isRefreshing = true
                        rlv.updateEmptyUI()
                    } else {
                        it.data?.let { list ->
                            adapter.submitList(list)
                            sfl.isRefreshing = false
                            rlv.updateEmptyUI()
                        }
                    }
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