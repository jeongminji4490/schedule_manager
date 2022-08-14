package com.example.newcalendar.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.newcalendar.alarm.AlarmFunctions
import com.example.newcalendar.db.AppDatabase
import com.example.newcalendar.viewmodel.AlarmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// 재부팅 시 실행될 리시버
class RestartAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val viewModel: AlarmViewModel by inject()
    private lateinit var functions: AlarmFunctions

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            functions = AlarmFunctions(context)
            coroutineScope.launch {
                val list = viewModel.getAllAlarm()
                list.forEach { alarm ->
                    val time = alarm.time
                    val code = alarm.alarm_code
                    val content = alarm.content
                    functions.callAlarm(time, code, content) // 알람 실행
                }
            }
        }
    }
}