package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item : ScheduleDataModel)

    @Query("DELETE FROM schedule WHERE alarm_code = :alarm_code")
    fun deleteItem(alarm_code : Int)

    @Query("select * from schedule")
    fun getAllSchedule() : LiveData<List<ScheduleDataModel>>
}