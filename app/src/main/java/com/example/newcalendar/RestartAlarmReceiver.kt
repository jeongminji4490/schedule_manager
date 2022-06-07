package com.example.newcalendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Database

class RestartAlarmReceiver : BroadcastReceiver() {

    private lateinit var alarmFunctions: AlarmFunctions

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            alarmFunctions = AlarmFunctions(context)
        }
    }
}