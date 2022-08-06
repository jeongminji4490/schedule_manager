package com.example.newcalendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newcalendar.model.entity.Schedule

@Dao
interface ScheduleDao { // 일정 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSchedule(item : Schedule)

    @Query("DELETE FROM schedule WHERE serialNum = :serialNum") // 일련번호로 삭제
    fun deleteSchedule(serialNum : Int)

    // '날짜' 에 해당하는 모든 데이터 가져오기
    @Query("SELECT * FROM schedule WHERE date = :date")
    fun getAllSchedule(date: String) : LiveData<List<Schedule>>

    // '일련 번호' 에 해당하는 모든 데이터 가져오기
    @Query("SELECT * FROM schedule WHERE serialNum = :serialNum")
    fun getSchedule(serialNum: Int) : Schedule
}