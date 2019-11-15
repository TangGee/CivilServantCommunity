package com.mdove.civilservantcommunity.feed

import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDbBean
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.civilservantcommunity.room.MainDb
import com.mdove.dependent.common.utils.TimeUtils
import java.util.*

/**
 * Created by MDove on 2019-11-15.
 */
object MainQuickSender {
    fun sendPlanInsertDB(content: String): FeedTimeLineFeedTodayPlansResp? {
        // TODO 先查再更新（插入），可优化
        val cusModuleId = "Z6566"
        val cusModuleName = "我的计划"
        val cusWrapper =
            SinglePlanBeanWrapper(
                SinglePlanBean(
                    AppConfig.getUserInfo()?.uid ?: UUID.randomUUID().toString()
                    , cusModuleId, cusModuleName, "1", "1", content
                ),
                SinglePlanType.CUSTOM_PLAN,
                SinglePlanStatus.NORMAL,
                null
            )
        return MainDb.db.todayPlansDao().getTodayPlansRecord()?.let { entity ->
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
            FeedTimeLineFeedTodayPlansResp(
                entity.id,
                entity.date,
                null,
                entity.createDate ?: TimeUtils.getDateFromSQL(),
                cusWrapper
            )
        } ?: let {
            // 当日没有任务，直接构建一个任务
            val cusEntity = TodayPlansEntity(
                date = System.currentTimeMillis(),
                createDate = TimeUtils.getDateFromSQL(),
                sucDate = null,
                resp = TodayPlansDbBean(
                    params = mutableListOf(
                        PlanModuleBean(
                            cusModuleId, cusModuleName,
                            mutableListOf(cusWrapper)
                        )
                    )
                )
            )
            MainDb.db.todayPlansDao().insert(cusEntity)?.let {
                FeedTimeLineFeedTodayPlansResp(
                    it,
                    cusEntity.date,
                    null,
                    cusEntity.createDate ?: TimeUtils.getDateFromSQL(),
                    cusWrapper
                )
            }
        }
    }
}