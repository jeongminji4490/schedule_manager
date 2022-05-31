package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ViewModel(private val sDao : ScheduleDao) : ViewModel(){

    fun getAllSchedule() : LiveData<List<ScheduleDataModel>> = sDao.getAllSchedule()

    fun addSchedule(data : ScheduleDataModel){ // 일정 추가
        sDao.addItem(data)
    }

    fun deleteSchedule(alarm_code : Int){ // 일정 삭제
        sDao.deleteItem(alarm_code)
    }
}