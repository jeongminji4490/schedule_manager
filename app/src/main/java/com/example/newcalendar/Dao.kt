package com.example.newcalendar

import androidx.lifecycle.LiveData
import androidx.room.*
import java.net.ContentHandler

@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMemo(item: MemoDataModel)

    @Query("DELETE FROM memo WHERE serialNum = :serialNum")
    fun deleteMemo(serialNum: Int)

    @Query("SELECT * FROM memo")
    fun getAllMemo() : LiveData<List<MemoDataModel>>

    @Query("UPDATE memo SET completion = :completion where serialNum = :serialNum")
    fun changeCompletion(completion: Boolean, serialNum: Int)

    @Query("UPDATE memo SET content = :content where serialNum = :serialNum")
    fun changeContent(content: String, serialNum: Int)
}

@Dao
interface ScheduleDao { // 일정 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSchedule(item : ScheduleDataModel)

    @Query("DELETE FROM schedule WHERE serialNum = :serialNum") // 일련번호로 삭제
    fun deleteSchedule(serialNum : Int)

    // 특정 날짜에 해당하는 모든 데이터 가져오기
    @Query("SELECT * FROM schedule WHERE date = :date")
    fun getAllSchedule(date: String) : LiveData<List<ScheduleDataModel>>

    @Query("SELECT * FROM schedule WHERE serialNum = :serialNum")
    fun getSchedule(serialNum: Int) : ScheduleDataModel

    // 특정 날짜에 해당하는 데이터 개수 가져오기
    @Query("SELECT count(*) FROM schedule WHERE date = :date")
    fun getCount(date: String) : LiveData<Int>
}

@Dao
interface EventDao { // 특정 날짜 저장용 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 날짜는 중복되지 않게 저장
    fun addDate(item: EventDataModel)

    @Query("DELETE FROM event WHERE date = :date") // 날짜로 삭제
    fun deleteDate(date : String)

    @Query("SELECT * FROM event")
    fun getAllDates() : LiveData<List<EventDataModel>>
}

@Dao
interface AlarmDao { // 재부팅 시 관리되어야 하는 알람 저장용 테이블 관련
    @Query("SELECT * FROM active_alarms")
    fun getAllAlarms() : List<AlarmDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 알람은 중복되지 않게 저장
    fun addAlarm(item: AlarmDataModel)

    @Query("DELETE FROM active_alarms WHERE alarm_code = :alarm_code") // 알람 코드로 삭제
    fun deleteAlarm(alarm_code: Int)
}