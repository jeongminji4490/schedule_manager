package com.example.newcalendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.newcalendar.db.ScheduleDao
import com.example.newcalendar.model.entity.Schedule

class ScheduleViewModel(
    private val sDao : ScheduleDao
) : ViewModel() {

    fun getAllSchedule(date: String) : LiveData<List<Schedule>>  // 모든 일정 조회
            = sDao.getAllSchedule(date)

    fun getSchedule(serialNum: Int) : Schedule // 특정 일정 조회
            = sDao.getSchedule(serialNum)

    fun addSchedule(data : Schedule){ // 일정 추가
        sDao.addSchedule(data)
    }

    fun deleteSchedule(serialNum : Int){ // 일정 삭제
        sDao.deleteSchedule(serialNum)
    }
}