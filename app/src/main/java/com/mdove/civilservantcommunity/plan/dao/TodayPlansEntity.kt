package com.mdove.civilservantcommunity.plan.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp

/**
 * Created by MDove on 2019-10-27.
 */
@Entity(tableName = "today_plans")
@TypeConverters(value = [TodayPlansConverter::class])
class TodayPlansEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "date")
    var date: Long,
    @ColumnInfo(name = "create_date")
    var createDate: String?,
    @ColumnInfo(name = "resp_json")
    val resp: FeedTimeLineFeedTodayPlansResp
)