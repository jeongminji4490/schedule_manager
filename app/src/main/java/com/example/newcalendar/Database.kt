package com.example.newcalendar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ScheduleDataModel::class, EventDataModel::class, AlarmDataModel::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val scheduleDao : ScheduleDao
    abstract val eventDao : EventDao
    abstract val alarmDao : AlarmDao

    companion object{
        private var instance: AppDatabase?=null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null){
                synchronized(AppDatabase::class){
                    instance=Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "SCHEDULE_DB"
                    ).build()
                }
            }
            return instance
        }
    }
}
