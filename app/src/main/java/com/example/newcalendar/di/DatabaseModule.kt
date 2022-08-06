package com.example.newcalendar.di

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import com.example.newcalendar.db.*

val databaseModule = module {

    fun provideDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "SCHEDULE_DB")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideMemoDao(database: AppDatabase) : MemoDao {
        return database.memoDao
    }

    fun provideDao(database: AppDatabase) : ScheduleDao {
        return database.scheduleDao
    }

    fun provideEventDao(database: AppDatabase) : EventDao {
        return database.eventDao
    }

    fun provideAlarmDao(database: AppDatabase) : AlarmDao {
        return database.alarmDao
    }

    single {
        provideDatabase(androidApplication())
    }

    single {
        provideMemoDao(get())
    }

    single {
        provideDao(get())
    }

    single {
        provideEventDao(get())
    }

    single {
        provideAlarmDao(get())
    }
}