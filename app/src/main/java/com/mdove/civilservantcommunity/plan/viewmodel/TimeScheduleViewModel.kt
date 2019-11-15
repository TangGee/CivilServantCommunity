package com.mdove.civilservantcommunity.plan.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.mdove.civilservantcommunity.plan.model.*

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
//        handlePlansData.filter {
//            it.timeSchedule != null
//        }.forEach {
//            val startTime = System.currentTimeMillis() +
//                    60 * 1000
//            val intent = Intent(context, TimeScheduleReceiver::class.java)
//            intent.action = "NOTIFICATION"
//            val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
//            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                manager.set(AlarmManager.RTC_WAKEUP, startTime, pi)
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                manager.setExact(AlarmManager.RTC_WAKEUP, startTime, pi)
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTime, pi)
//            }
//        }
    }
}