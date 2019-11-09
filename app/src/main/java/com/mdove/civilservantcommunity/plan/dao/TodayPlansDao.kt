package com.mdove.civilservantcommunity.plan.dao

import androidx.room.*

@Dao
interface TodayPlansDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(resp: TodayPlansEntity): Long?

    @Query("SELECT * FROM today_plans WHERE id = :id")
    fun getFeedTodayPlan(id: Long): TodayPlansEntity?

    @Query("SELECT * FROM today_plans")
    fun getFeedTodayPlans(): List<TodayPlansEntity>?

    @Query("SELECT * FROM today_plans WHERE create_date = :todayTime")
    fun getTodayPlansRecord(todayTime: String): TodayPlansEntity?

    @Delete
    fun deleteTodayPlanRecord(entity: TodayPlansEntity)

    @Update
    fun update(bean: TodayPlansEntity)

    @Query("SELECT count(*) FROM today_plans")
    fun getFeedTodayPlanCounts(): Int
}