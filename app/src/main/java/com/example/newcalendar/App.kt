package com.example.newcalendar

import android.app.Application
import org.koin.android.ext.koin.androidContext
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
                module,
                scheduleModule,
                viewModel
            )
        }
    }
}