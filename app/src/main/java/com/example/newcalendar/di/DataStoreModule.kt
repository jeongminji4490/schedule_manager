package com.example.newcalendar.di

import com.example.newcalendar.DateSaveModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        DateSaveModule(androidApplication())
    }
}