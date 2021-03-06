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
import com.mdove.civilservantcommunity.account.gotoAccountActivity
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.detailfeed.DetailFeedActivity
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.feed.adapter.*
import com.mdove.civilservantcommunity.feed.bean.FeedArticleFeedResp
import com.mdove.civilservantcommunity.feed.bean.FeedQuestionFeedResp
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.feed.bean.FeedTodayPlansCheckParams
import com.mdove.civilservantcommunity.feed.viewmodel.HideRecordParams
import com.mdove.civilservantcommunity.setting.utils.HideRecorder
import com.mdove.civilservantcommunity.setting.utils.IHideRecorderObserver
import com.mdove.civilservantcommunity.feed.viewmodel.LoadType
import com.mdove.civilservantcommunity.feed.viewmodel.MainFeedViewModel
import com.mdove.civilservantcommunity.plan.activity.HistoryPlansActivity
import com.mdove.civilservantcommunity.plan.activity.gotoPlanActivity
import com.mdove.civilservantcommunity.plan.activity.gotoTimeScheduleActivity
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDbBean
import com.mdove.civilservantcommunity.plan.model.SinglePlanStatus
import com.mdove.civilservantcommunity.plan.model.TimeScheduleStatus
import com.mdove.civilservantcommunity.punch.bean.PunchReq
import com.mdove.civilservantcommunity.punch.viewmodel.PunchViewModel
import com.mdove.civilservantcommunity.question.DetailQuestionActivity
import com.mdove.civilservantcommunity.question.bean.QuestionReqParams
import com.mdove.civilservantcommunity.question.viewmodel.QuestionViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.civilservantcommunity.setting.SettingActivity
import com.mdove.civilservantcommunity.ugc.MainUGCActivity
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.recyclerview.PaddingDecoration
import com.mdove.dependent.common.recyclerview.PaddingType
import com.mdove.dependent.common.threadpool.FastMain
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_main_feed.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedFragment : BaseFragment(), IHideRecorderObserver {
    private lateinit var feedViewModel: MainFeedViewModel
    private lateinit var punchViewModel: PunchViewModel
    private lateinit var mQuestionViewModel: QuestionViewModel
    private val adapter = MainFeedAdapter(object : OnMainFeedClickListener {
        override fun onClick(type: Int, respFeed: FeedArticleFeedResp?) {
            when (type) {
                MainFeedAdapter.TYPE_FEED_PUNCH -> {
                    clickPunch()
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
                MainFeedAdapter.CLICK_MAIN_FEED_LOGIN -> {
                    clickMainFeedLogin()
                }
                MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_TIPS -> {
                    clickPlan()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_ME -> {
                    clickMePage()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_HISTORY_PLANS -> {
                    clickHistoryPlans()
                }
                MainFeedAdapter.CLICK_QUICK_BTN_TIME_SCHEDULE -> {
                    clickTimeSchedule()
                }
                MainFeedAdapter.TYPE_FEED_TIME_LINE_FEED_TODAY_PLAN_BTN_APPLY_OLD -> {
                    applyOldPlans()
                }
                else -> {
                    respFeed?.article?.aid?.let { aid ->
                        context?.let { context ->
                            DetailFeedActivity.gotoFeedDetail(context, DetailFeedParams(aid))
                        }
                    }
                }
            }
        }
    }, object : OnNormalFeedListener {
        override fun onClickSetting() {
            activity?.let {
                SettingActivity.goto(it)
            }
        }

        override fun onClickGoUGC() {
            clickUGC()
        }

        override fun loadedMore() {
            feedViewModel.reqFeed(LoadType.LOAD_MORE)
        }

        override fun onSendNewPlanClick(content: String) {
            launch {
                //                showLoading()
                withContext(MDoveBackgroundPool) {
                    MainQuickSender.sendPlanInsertDB(content)?.let {
                        withContext(FastMain) {
                            feedViewModel.editNewPlanToFeedLiveData.value = it
                        }
                    }
                }
//                dismissLoading()
            }
        }
    }, object : OnMainFeedQuickClickListener {
        override fun onClick(type: Int, questionResp: FeedQuestionFeedResp) {
            context?.let { context ->
                questionResp.question.qid?.let {
                    DetailQuestionActivity.gotoQuestion(context, QuestionReqParams(it))
                }
            }
        }
    }, object : OnMainFeedTodayPlanCheckListener {
        override fun onCheck(resp: FeedTimeLineFeedTodayPlansResp, isCheck: Boolean) {
            launch {
                //                showLoading()
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
//                dismissLoading()
                feedViewModel.checkTodayPlanLiveData.value =
                    FeedTodayPlansCheckParams(resp, isCheck)
            }
        }
    }, object : OnMainFeedHideClickListener {
        override fun onClick(type: Int) {
            HideRecorder.addHideRecord(type)
        }
    })

    private fun applyOldPlans() {
        feedViewModel.applyOldPlansLiveData.value = ""
    }

    private fun clickMePage() {
        context?.let { context ->
            AppConfig.getUserInfo()?.let {
                MePageActivity.gotoMePage(context)
            } ?: also {
                launch {
                    (activity as? ActivityLauncher)?.gotoAccountActivity(context)?.params?.let {
                        feedViewModel.appConfigLiveData.value = it
                    }
                }
            }
        }
    }

    override fun onChange(hides: List<Int>?, hide: Int?, isHide: Boolean) {
        hide?.let {
            feedViewModel.hideLiveData.value = HideRecordParams(hide, isHide)
        }
    }

    private fun clickHistoryPlans() {
        context?.let {
            HistoryPlansActivity.goto(it)
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

    private fun clickMainFeedLogin() {
        AppConfig.getUserInfo()?.let {
            clickMePage()
        } ?: also {
            launch {
                (activity as? ActivityLauncher)?.let {
                    it.gotoAccountActivity(context!!).params?.let {
                        feedViewModel.appConfigLiveData.value = it
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
            mQuestionViewModel = ViewModelProviders.of(it).get(QuestionViewModel::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        feedViewModel.appConfigLiveData.value = AppConfig.getUserInfo()
        HideRecorder.registerObserver(this)
    }

    override fun onStop() {
        super.onStop()
        HideRecorder.unregisterObserver(this)
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
        rlv.addItemDecoration(PaddingDecoration(8, PaddingType.BOTTOM))
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

        mQuestionViewModel.saveAnswerLiveData.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    dismissLoading()
                    it.data?.message?.let {
                        ToastUtil.toast(it)
                    }
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    it.data?.message?.let {
                        ToastUtil.toast(it)
                    }
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