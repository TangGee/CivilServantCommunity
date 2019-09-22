package com.mdove.civilservantcommunity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "punch_record")
@TypeConverters(value = [PunchRecordConverter::class])
class PunchRecordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @SerializedName("date")
    var date: Long,
    @ColumnInfo(name = "punch_json")
    val record: PunchRecordBean
)