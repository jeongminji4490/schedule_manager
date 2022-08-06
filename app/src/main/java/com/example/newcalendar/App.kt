package com.example.newcalendar

import android.app.Application
import androidx.datastore.dataStore
import com.example.newcalendar.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    companion object{
        private lateinit var app : App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        startKoin {
            androidContext(this@App)
            modules(
                dataStoreModule,
                databaseModule,
                memoViewModelModule,
                scheduleViewModelModule,
                eventViewModelModule,
                alarmViewModelModule
            )
        }
    }
}