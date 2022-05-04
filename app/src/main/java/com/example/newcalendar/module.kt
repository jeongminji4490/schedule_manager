package com.example.newcalendar

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single {
        DateSaveModule(androidApplication())
    }
}

val scheduleModule = module {

    fun provideDatabase(application: Application) : Database {
        return Room.databaseBuilder(application, Database::class.java, "SCHEDULE_DB")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: Database) : ScheduleDao {
        return database.scheduleDao
    }

    single {
        provideDatabase(androidApplication())
    }

    single {
        provideDao(get())
    }
}

val viewModel = module {
    viewModel {
        ViewModel(get())
    }
}