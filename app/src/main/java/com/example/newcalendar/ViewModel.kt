package com.example.newcalendar

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ViewModel(private val mDao: MemoDao,
                private val sDao : ScheduleDao,
                private val eDao: EventDao,
                private val aDao: AlarmDao) : ViewModel(){

    fun getAllMemo() : LiveData<List<MemoDataModel>> = mDao.getAllMemo()

    fun getAllSchedule(date: String) : LiveData<List<ScheduleDataModel>> = sDao.getAllSchedule(date)

    fun getCount(date: String) : LiveData<Int> = sDao.getCount(date)

    fun getSchedule(serialNum: Int) : ScheduleDataModel = sDao.getSchedule(serialNum)

    fun getAllDates() : LiveData<List<EventDataModel>> = eDao.getAllDates()

    fun getAllAlarms() : List<AlarmDataModel> = aDao.getAllAlarms()

    fun addMemo(item : MemoDataModel){
        mDao.addMemo(item)
    }

    fun addSchedule(data : ScheduleDataModel){ // 일정 추가
        sDao.addSchedule(data)
    }

    fun addDate(data : EventDataModel){ // 날짜 추가
        eDao.addDate(data)
    }

    fun addAlarm(data : AlarmDataModel){ // 날짜 추가
        aDao.addAlarm(data)
    }

    fun deleteMemo(serialNum: Int){
        mDao.deleteMemo(serialNum)
    }

    fun deleteSchedule(serialNum : Int){ // 일정 삭제
        sDao.deleteSchedule(serialNum)
    }

    fun deleteDate(date : String){ // 날짜 삭제
        eDao.deleteDate(date)
    }

    fun deleteAlarm(alarm_code: Int){ // 날짜 삭제
        aDao.deleteAlarm(alarm_code)
    }

}