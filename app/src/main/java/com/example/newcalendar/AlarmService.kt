package com.example.newcalendar

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.view.View
import org.koin.android.ext.android.inject

class AlarmService: Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented");
    }
}