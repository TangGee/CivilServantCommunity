package com.mdove.civilservantcommunity.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PunchRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(state: PunchRecordEntity): Long?

    @Query("SELECT * FROM punch_record WHERE id = :id")
    fun getState(id: Long): PunchRecordEntity?

    @Query("SELECT count(*) FROM punch_record")
    fun getPunchCounts(): Int

    @Query("SELECT * FROM punch_record ORDER BY date DESC LIMIT 1")
    fun getLastPunch(): PunchRecordEntity?
}