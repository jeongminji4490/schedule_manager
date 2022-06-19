package com.example.newcalendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoDataModel(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val content: String,
    val completion : Boolean
)

//@Entity(tableName = "completion")
//data class CompletionDataModel(
//    @PrimaryKey(autoGenerate = true)
//    val serialNum: Int,
//
//)

@Entity(tableName = "schedule") // 일정 테이블
data class ScheduleDataModel(
    @PrimaryKey(autoGenerate = true)
    var serialNum : Int = 0, //일련번호
    var date : String, //날짜(년/월/일)
    var content : String, //제목
    var alarm : String, // 알람시간(:)
    var hour : String, // 알람 시
    var minute : String, // 알람 분
    var alarm_code : Int, // 알람요청코드
    var importance : Int //중요도
)

@Entity(tableName = "event") // 특정 날짜(일정이 저정된) 저장용 테이블
data class EventDataModel(
    @PrimaryKey
    var date: String
)

@Entity(tableName = "active_alarms") // 재부팅 시 활성화 되어야하는 알람 테이블
data class AlarmDataModel(
    @PrimaryKey(autoGenerate = true)
    var alarm_code : Int, // 알람 요청코드
    var time : String, // 시간
    var content : String // 알람 내용
)
