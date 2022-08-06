package com.example.newcalendar.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event") // 특정 날짜(일정이 저정된) 저장용 테이블
data class Event(
    @PrimaryKey
    var date: String // 날짜
)

