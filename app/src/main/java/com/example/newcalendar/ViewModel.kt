package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ViewModel(private val sDao : ScheduleDao, private val eDao: EventDao, private val aDao: AlarmDao) : ViewModel(){

    fun getAllSchedule() : LiveData<List<ScheduleDataModel>> = sDao.getAllSchedule()

    fun getAllDates() : LiveData<List<EventDataModel>> = eDao.getAllDates()

    fun getAllAlarms() : LiveData<List<AlarmDataModel>> = aDao.getAllAlarms()

    fun addSchedule(data : ScheduleDataModel){ // 일정 추가
        sDao.addItem(data)
    }

    fun deleteSchedule(serialNum : Int){ // 일정 삭제
        sDao.deleteItem(serialNum)
    }

    fun addDate(data : EventDataModel){ // 날짜 추가
        eDao.addDate(data)
    }

    fun deleteDate(date : String){ // 날짜 삭제
        eDao.deleteDate(date)
    }

    fun addAlarm(data : AlarmDataModel){ // 날짜 추가
        aDao.addAlarm(data)
    }

    fun deleteAlarm(alarm_code: Int){ // 날짜 삭제
        aDao.deleteAlarm(alarm_code)
    }
}