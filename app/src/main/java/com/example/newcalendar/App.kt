package com.example.newcalendar

import android.app.Application

class App : Application() {
    private lateinit var datastore : DataStoreModule

    companion object{
        private lateinit var app : App
        fun getInstance() : App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        datastore = DataStoreModule(this)
    }

    fun getDataStore() : DataStoreModule = datastore
}