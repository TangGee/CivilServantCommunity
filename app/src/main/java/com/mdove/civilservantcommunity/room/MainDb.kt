package com.mdove.civilservantcommunity.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mdove.civilservantcommunity.MyApplication

@Database(entities = [PunchRecordEntity::class], version = 1, exportSchema = true)
abstract class MainDb: RoomDatabase() {
    companion object {
        val db = Room.databaseBuilder(
            MyApplication.getInst().applicationContext,
            MainDb::class.java,
            "ugc_ve_db"
            ).build()
    }

    abstract fun punchRecordDao(): PunchRecordDao
}