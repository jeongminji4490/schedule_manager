package com.example.newcalendar.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newcalendar.*
import com.example.newcalendar.model.entity.Alarm
import com.example.newcalendar.model.entity.Event
import com.example.newcalendar.model.entity.Memo
import com.example.newcalendar.model.entity.Schedule

@Database(
    entities = [
        Memo::class,
        Schedule::class,
        Event::class,
        Alarm::class],
    version = 11,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val memoDao : MemoDao
    abstract val scheduleDao : ScheduleDao
    abstract val eventDao : EventDao
    abstract val alarmDao : AlarmDao

    companion object{
        private var instance: AppDatabase?=null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null){
                synchronized(AppDatabase::class){
                    instance =Room.databaseBuilder(
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
