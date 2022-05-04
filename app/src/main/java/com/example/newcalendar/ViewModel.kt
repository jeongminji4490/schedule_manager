package com.example.newcalendar

import androidx.lifecycle.ViewModel

class ViewModel(val sDao : ScheduleDao) : ViewModel(){

    fun addSchedule(data : ScheduleData){
        sDao.addItem(data)
    }
}