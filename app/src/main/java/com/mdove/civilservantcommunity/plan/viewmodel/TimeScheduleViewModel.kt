package com.mdove.civilservantcommunity.plan.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.mdove.civilservantcommunity.MyApplication
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.dependent.common.utils.Func1
import com.mdove.dependent.common.utils.NotificationUtils
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.MainFeedActivity
import com.mdove.civilservantcommunity.plan.receiver.TimeScheduleReceiver


/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleViewModel : ViewModel() {
    val paramsLiveData = MutableLiveData<TimeScheduleParams>()
    // 添加到Time块中Plan，需要从Rlv中移除
    val removeSinglePlanBean = MutableLiveData<SinglePlanBean>()
    // 从Time块中释放到Rlv中
    val releaseSinglePlanBean = MutableLiveData<SinglePlanBean>()
    // Rlv被touch的View的显示与否
    val changeSinglePlanBean =
        MutableLiveData<Pair<SinglePlanBean, TimeSchedulePlansStatus>>()

    private val handlePlansData = mutableListOf<TimeSchedulePlansParams>()

    val plansLiveData = MediatorLiveData<List<TimeScheduleBaseParams>>().apply {
        addSource(paramsLiveData) {
            value = it.data.takeIf {
                it.isNotEmpty()
            }?.map {
                handlePlansData.add(it)
                it
            } ?: let {
                mutableListOf(TimeScheduleEmptyParams())
            }
        }

        addSource(releaseSinglePlanBean) { release ->
            value = value?.let {
                val newData = mutableListOf<TimeScheduleBaseParams>()
                newData.add(TimeSchedulePlansParams(release, TimeSchedulePlansStatus.SHOW))
                newData.addAll(it)
                newData
            }
        }

        addSource(removeSinglePlanBean) { remove ->
            value = value?.filterNot {
                (it as? TimeSchedulePlansParams)?.data?.moduleId == remove.moduleId &&
                        (it as? TimeSchedulePlansParams)?.data?.content == remove.content
            }
        }

        addSource(changeSinglePlanBean) { change ->
            value = value?.map {
                if ((it as? TimeSchedulePlansParams)?.data?.moduleId == change.first.moduleId
                    && (it as? TimeSchedulePlansParams)?.data?.content == change.first.content
                ) {
                    (it as? TimeSchedulePlansParams)?.copy(status = change.second) ?: it
                } else {
                    it
                }
            }
        }
    }


    fun putOrderTime(data: SinglePlanBean, time: Pair<Long, Long>) {
        handlePlansData.forEach {
            if (it.data.moduleId == data.moduleId && it.data.content == data.content) {
                it.timeSchedule = time
            }
        }
    }

    fun hasPlans(): Boolean {
        return !plansLiveData.value.isNullOrEmpty()
    }

    fun createTimeSchedulePlansToFeed(): TimeScheduleParams {
        return TimeScheduleParams(handlePlansData)
    }

    fun createAlarm(context: Context) {
        handlePlansData.filter {
            it.timeSchedule != null
        }.forEach { it ->
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, TimeScheduleReceiver::class.java).let { intent ->
                intent.action = TimeScheduleReceiver.ACTION
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmMgr.setExact(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    it.timeSchedule?.first!!,
                    alarmIntent
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    System.currentTimeMillis() + 60 * 1000,
                    alarmIntent
                )
            }
        }
    }
}