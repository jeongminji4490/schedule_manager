package com.example.newcalendar

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val module = module {
    single {
        DateSaveModule(androidApplication())
    }
}