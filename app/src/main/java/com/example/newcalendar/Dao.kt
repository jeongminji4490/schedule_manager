package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item : ScheduleDataModel)

    @Query("select * from schedule")
    fun getAllSchedule() : LiveData<List<ScheduleDataModel>>
}