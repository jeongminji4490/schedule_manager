package com.example.newcalendar

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ScheduleData::class), version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract val scheduleDao : ScheduleDao
}
