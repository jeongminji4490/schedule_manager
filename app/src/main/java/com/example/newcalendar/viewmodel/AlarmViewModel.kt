package com.example.newcalendar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newcalendar.db.AlarmDao
import com.example.newcalendar.model.entity.Alarm

class AlarmViewModel(
    private val aDao : AlarmDao
): ViewModel() {
    fun getAllAlarm() : List<Alarm> = aDao.getAllAlarms()

    fun addAlarm(data : Alarm){ // 날짜 추가
        aDao.addAlarm(data)
    }

    fun deleteAlarm(alarm_code: Int){ // 날짜 삭제
        aDao.deleteAlarm(alarm_code)
    }
}