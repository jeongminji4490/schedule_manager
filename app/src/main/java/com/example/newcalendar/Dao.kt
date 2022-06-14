package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScheduleDao { // 일정 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item : ScheduleDataModel)

    @Query("DELETE FROM schedule WHERE serialNum = :serialNum") // 일련번호로 삭제
    fun deleteItem(serialNum : Int)

    @Query("select * from schedule")
    fun getAllSchedule() : LiveData<List<ScheduleDataModel>>

    @Query("select * from schedule where serialNum = :serialNum")
    fun getSchedule(serialNum: Int) : ScheduleDataModel
}

@Dao
interface EventDao { // 특정 날짜 저장용 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 날짜는 중복되지 않게 저장
    fun addDate(item: EventDataModel)

    @Query("DELETE FROM event WHERE date = :date") // 날짜로 삭제
    fun deleteDate(date : String)

    @Query("select * from event")
    fun getAllDates() : LiveData<List<EventDataModel>>
}

@Dao
interface AlarmDao { // 재부팅 시 관리되어야 하는 알람 저장용 테이블 관련
    @Query("select * from active_alarms")
    fun getAllAlarms() : List<AlarmDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 알람은 중복되지 않게 저장
    fun addAlarm(item: AlarmDataModel)

    @Query("DELETE FROM active_alarms WHERE alarm_code = :alarm_code") // 알람 코드로 삭제
    fun deleteAlarm(alarm_code: Int)
}