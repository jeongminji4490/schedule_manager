package com.example.newcalendar.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newcalendar.model.entity.Alarm

@Dao
interface AlarmDao { // 재부팅 시 관리되어야 하는 알람 저장용 테이블 관련
    @Query("SELECT * FROM active_alarms")
    fun getAllAlarms() : List<Alarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 알람은 중복되지 않게 저장
    fun addAlarm(item: Alarm)

    @Query("DELETE FROM active_alarms WHERE alarm_code = :alarm_code") // 알람 코드로 삭제
    fun deleteAlarm(alarm_code: Int)
}