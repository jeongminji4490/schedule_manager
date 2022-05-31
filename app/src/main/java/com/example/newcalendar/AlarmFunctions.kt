package com.example.newcalendar

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmFunctions(private val context: Context){

    private lateinit var pendingIntent: PendingIntent
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun callAlarm(from : String, alarm_code : Int, content : String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, AlarmReceiver::class.java) //리시버로 전달될 인텐트 설정
        receiverIntent.putExtra("alarm_rqCode", alarm_code) //요청 코드를 리시버에 전달
        receiverIntent.putExtra("content", content) //수정_일정 제목을 리시버에 전달

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent=PendingIntent.getBroadcast(context,alarm_code,receiverIntent,PendingIntent.FLAG_IMMUTABLE);
            Log.e("AlarmFunctions", alarm_code.toString());
        }else{
            pendingIntent=PendingIntent.getBroadcast(context,alarm_code,receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            Log.e("AlarmFunctions", alarm_code.toString());
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd H:mm:ss")
        var datetime = Date()
        try {
            datetime = dateFormat.parse(from)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.e("dateFormat", datetime.toString())

        val calendar = Calendar.getInstance()
        calendar.time = datetime

        //API 23(android 6.0) 이상(해당 api 레벨부터 도즈모드 도입으로 setExact 사용 시 알람이 울리지 않음)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent);
    }

    fun cancelAlarm(viewModel: ViewModel, alarm_code: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        //val pendingIntent : PendingIntent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent=PendingIntent.getBroadcast(context,alarm_code,intent,PendingIntent.FLAG_IMMUTABLE);
            Log.e("AlarmFunctions", alarm_code.toString());
        }else{
            pendingIntent=PendingIntent.getBroadcast(context,alarm_code,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            Log.e("AlarmFunctions", alarm_code.toString());
        }

        alarmManager.cancel(pendingIntent)
        ioScope.launch {
            viewModel.deleteSchedule(alarm_code)
        }
    }
}