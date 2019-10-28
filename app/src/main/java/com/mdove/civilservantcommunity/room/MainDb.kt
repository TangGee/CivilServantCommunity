package com.mdove.civilservantcommunity.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mdove.civilservantcommunity.MyApplication
import com.mdove.civilservantcommunity.plan.dao.TodayPlansDao
import com.mdove.civilservantcommunity.plan.dao.TodayPlansEntity

@Database(
    entities = [PunchRecordEntity::class, TodayPlansEntity::class],
    version = 3,
    exportSchema = true
)
abstract class MainDb : RoomDatabase() {
    companion object {
        val db = Room.databaseBuilder(
            MyApplication.getInst().applicationContext,
            MainDb::class.java,
            "civil_main_db"
        ).addMigrations(object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `today_plans` (`id` INTEGER NOT NULL,`date` INTEGER NOT NULL ,`resp_json` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }, object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `today_plans` ADD COLUMN `create_date` INTEGER;")
            }
        }).build()
    }

    abstract fun punchRecordDao(): PunchRecordDao
    abstract fun todayPlansDao(): TodayPlansDao
}