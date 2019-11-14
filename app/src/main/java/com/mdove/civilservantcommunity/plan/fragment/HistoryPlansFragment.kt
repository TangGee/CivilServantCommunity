package com.mdove.civilservantcommunity.plan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.plan.adapter.EditPlanModuleAdapter
import com.mdove.civilservantcommunity.plan.viewmodel.HistoryPlansViewModel
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.threadpool.MDoveBackgroundPool
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
        initHistoryTime()
    }

    private fun initHistoryTime() {
        recyclerView.adapter = EditPlanModuleAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyPlansViewModel.selectRecordLiveData.observe(this, androidx.lifecycle.Observer {
            it?.let {
                (recyclerView.adapter as? EditPlanModuleAdapter)?.submitList(it)
            }
        })

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
}