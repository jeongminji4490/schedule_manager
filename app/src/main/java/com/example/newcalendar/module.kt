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

    fun provideDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "SCHEDULE_DB")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: AppDatabase) : ScheduleDao {
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