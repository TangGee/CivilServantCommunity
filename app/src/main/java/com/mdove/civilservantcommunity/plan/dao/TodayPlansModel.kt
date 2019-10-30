package com.mdove.civilservantcommunity.plan.dao

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.mdove.civilservantcommunity.plan.PlanModuleBean
import kotlinx.android.parcel.Parcelize

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
    @ColumnInfo(name = "suc_date")
    var sucDate: Long?,
    @ColumnInfo(name = "resp_json")
    var resp: TodayPlansDbBean
)

@Parcelize
data class TodayPlansDbBean(
    @SerializedName("data")
    val params: List<PlanModuleBean>
) : Parcelable