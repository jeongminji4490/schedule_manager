package com.example.newcalendar.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule") // 일정 테이블
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    var serialNum : Int = 0, // 일련번호
    var date : String, // 날짜(년/월/일)
    var content : String, // 제목
    var alarm : String, // 알람시간(:)
    var hour : String, // 알람 시
    var minute : String, // 알람 분
    var alarm_code : Int, // 알람 요청코드
    var importance : Int // 중요도
)

