package com.example.newcalendar

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel(
    private val mDao: MemoDao,
    private val sDao : ScheduleDao,
    private val eDao: EventDao,
    private val aDao: AlarmDao) : ViewModel(){

    // Memo
    fun getAllMemo() : LiveData<List<MemoDataModel>> = mDao.getAllMemo()

    fun addMemo(item : MemoDataModel){
        mDao.addMemo(item)
    }

    fun deleteMemo(serialNum: Int){
        mDao.deleteMemo(serialNum)
    }

    fun changeCompletion(completion: Boolean, serialNum: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            mDao.changeCompletion(completion, serialNum)
        }
    }

    suspend fun changeContent(content: String, serialNum: Int) {
        mDao.changeContent(content, serialNum)
    }

    // Schedule
    fun getAllSchedule(date: String) : LiveData<List<ScheduleDataModel>> = sDao.getAllSchedule(date)

    fun getSchedule(serialNum: Int) : ScheduleDataModel = sDao.getSchedule(serialNum)

    fun addSchedule(data : ScheduleDataModel){ // 일정 추가
        sDao.addSchedule(data)
    }

    fun deleteSchedule(serialNum : Int){ // 일정 삭제
        sDao.deleteSchedule(serialNum)
    }

    // Event
    fun getAllDates() : LiveData<List<EventDataModel>> = eDao.getAllDates()

    fun addDate(data : EventDataModel){ // 날짜 추가
        eDao.addDate(data)
    }

    fun deleteDate(date : String){ // 날짜 삭제
        eDao.deleteDate(date)
    }

    // Alarm
    fun getAllAlarms() : List<AlarmDataModel> = aDao.getAllAlarms()

    fun addAlarm(data : AlarmDataModel){ // 날짜 추가
        aDao.addAlarm(data)
    }

    fun deleteAlarm(alarm_code: Int){ // 날짜 삭제
        aDao.deleteAlarm(alarm_code)
    }
}