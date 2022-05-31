package com.example.newcalendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleDataModel(
    @PrimaryKey(autoGenerate = true)
    var serialNum : Int = 0, //일련번호
    var date : String, //날짜(년/월/일)
    var content : String, //제목
    var alarm : String, //알람시간(:)
    var alarm_code : Int, // 알람요청코드
    var importance : Int //중요도
)
