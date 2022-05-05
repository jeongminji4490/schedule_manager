package com.example.newcalendar

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScheduleDataModel::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract val scheduleDao : ScheduleDao
}
