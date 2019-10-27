package com.mdove.civilservantcommunity.plan.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodayPlansDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(resp: TodayPlansEntity): Long?

    @Query("SELECT * FROM today_plans WHERE id = :id")
    fun getFeedTodayPlan(id: Long): TodayPlansEntity?

    @Query("SELECT count(*) FROM today_plans")
    fun getFeedTodayPlanCounts(): Int
}