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

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDate(item: EventDataModel)

    @Query("DELETE FROM event WHERE date = :date")
    fun deleteDate(date : String)

    @Query("select * from event")
    fun getAllDates() : LiveData<List<EventDataModel>>
}

@Dao
interface AlarmDao {
    @Query("select * from active_alarms")
    fun getAllAlarms() : LiveData<List<AlarmDataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAlarm(item: AlarmDataModel)

    @Query("DELETE FROM active_alarms WHERE alarm_code = :alarm_code")
    fun deleteAlarm(alarm_code: Int)
}