package com.example.newcalendar.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.newcalendar.alarm.receiver.AlarmReceiver
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmFunctions(private val context: Context?){

    private lateinit var pendingIntent: PendingIntent

    @SuppressLint("UnspecifiedImmutableFlag")
    fun callAlarm(time : String, alarm_code : Int, content : String){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, AlarmReceiver::class.java) //리시버로 전달될 인텐트 설정

        receiverIntent.apply {
            putExtra("alarm_rqCode", alarm_code) //요청 코드를 리시버에 전달
            putExtra("content", content) //수정_일정 제목을 리시버에 전달
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getBroadcast(context,alarm_code,receiverIntent,PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getBroadcast(context,alarm_code,receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        val dateFormat = SimpleDateFormat("yyyy-MM-dd H:mm:ss")
        var datetime = Date()
        try {
            datetime = dateFormat.parse(time) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val calendar = Calendar.getInstance()
        calendar.time = datetime

        //API 23(android 6.0) 이상(해당 api 레벨부터 도즈모드 도입으로 setExact 사용 시 알람이 울리지 않음)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(alarm_code: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getBroadcast(context,alarm_code,intent,PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getBroadcast(context,alarm_code,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }
}