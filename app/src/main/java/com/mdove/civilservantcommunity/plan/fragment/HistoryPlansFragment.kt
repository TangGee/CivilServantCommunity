package com.mdove.civilservantcommunity.plan.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.plan.adapter.HistoryPlansAdapter
import com.mdove.civilservantcommunity.plan.viewmodel.HistoryPlansViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.recyclerview.PaddingDecoration
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
import com.mdove.dependent.common.utils.TimeUtils
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import com.mdove.dependent.common.view.calendar.painter.InnerPainter
import kotlinx.android.synthetic.main.fragment_history_plan.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import java.util.*

/**
 * Created by MDove on 2019-11-13.
 */
class HistoryPlansFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_plan, container, false)
    }

    private lateinit var historyPlansViewModel: HistoryPlansViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyPlansViewModel =
            ViewModelProviders.of(activity!!).get(HistoryPlansViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_toolbar.setTitle("历史计划")
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setColorForAll(Color.BLACK)
        initHistoryTime()
    }

    private fun initHistoryTime() {
        recyclerView.adapter = HistoryPlansAdapter()
        recyclerView.addItemDecoration(PaddingDecoration(8))
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyPlansViewModel.selectRecordLiveData.observe(this, androidx.lifecycle.Observer {
            it?.let {
                (recyclerView.adapter as? HistoryPlansAdapter)?.submitList(it)
            }
        })

        //初始化进入时的计划
        historyPlansViewModel.selectTimeLiveData.value = TimeUtils.getDateFromSQL()

        calendar.setOnCalendarChangedListener { _, _, _, localDate ->
            historyPlansViewModel.selectTimeLiveData.value = localDate.toString()
        }

        launch {
            showLoading()
            withContext(MDoveBackgroundPool) {
                MainDb.db.todayPlansDao().getFeedTodayPlans()?.let {
                    it.map {
                        LocalDate.fromDateFields(Date(it.date))
                    }
                }
            }?.let {
                dismissLoading()
                calendar.calendarPainter = InnerPainter(calendar, it)
            }
        }
    }

    fun inCalendarTouchScope(x: Int, y: Int): Boolean {
        val w = calendar.getChildAt(0).width
        val h = calendar.getChildAt(0).height
        return x in 1 until w && y in 1 until h
    }
}