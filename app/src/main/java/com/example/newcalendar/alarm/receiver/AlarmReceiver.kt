package com.example.newcalendar.alarm.receiver

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.newcalendar.alarm.service.AlarmService
import com.example.newcalendar.alarm.AlarmFunctions
import com.example.newcalendar.db.AppDatabase
import com.example.newcalendar.viewmodel.AlarmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class AlarmReceiver() : BroadcastReceiver(), KoinComponent {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val viewModel: AlarmViewModel by inject()
    private lateinit var functions: AlarmFunctions

    private lateinit var manager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    //오레오 이상은 반드시 채널을 설정해줘야 Notification 작동함
    companion object{
        const val CHANNEL_ID = "channel"
        const val CHANNEL_NAME = "channel1"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //NotificationChannel 인스턴스를 createNotificationChannel()에 전달하여 앱 알림 채널을 시스템에 등록
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        builder = NotificationCompat.Builder(context, CHANNEL_ID)

        val intent2 = Intent(context, AlarmService::class.java)
        val requestCode = intent?.extras!!.getInt("alarm_rqCode")
        val title = intent.extras!!.getString("content")

        Log.e("AlarmReceiver is Called", requestCode.toString())

        functions = AlarmFunctions(context)
        coroutineScope.launch {
            viewModel.deleteAlarm(requestCode)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S)
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_UPDATE_CURRENT)


        val notification = builder.setContentTitle(title)
            .setContentText("SCHEDULE MANAGER")
            .setSmallIcon(R.drawable.btn_star)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(1, notification)
    }
}