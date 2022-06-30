package com.example.newcalendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 재부팅 시 실행될 리시버
class RestartAlarmReceiver : BroadcastReceiver() {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private lateinit var functions: AlarmFunctions

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            functions = AlarmFunctions(context)
            coroutineScope.launch {
                val db = AppDatabase.getInstance(context)
                val list = db!!.alarmDao.getAllAlarms()
                val size = db.alarmDao.getAllAlarms().size
                list.let {
                    for (i in 0 until size){
                        val time = list[i].time
                        val code = list[i].alarm_code
                        val content = list[i].content
                        functions.callAlarm(time, code, content) // 알람 실행
                    }
                }
            }
        }
    }
}