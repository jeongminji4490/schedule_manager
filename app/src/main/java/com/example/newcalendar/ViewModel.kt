package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ViewModel(val sDao : ScheduleDao) : ViewModel(){

    fun addSchedule(data : ScheduleDataModel){
        sDao.addItem(data)
    }

    fun getAllSchedule() : LiveData<List<ScheduleDataModel>> = sDao.getAllSchedule()

    fun deleteSchedule(rqCode : Int){
        sDao.deleteItem(rqCode)
    }
}