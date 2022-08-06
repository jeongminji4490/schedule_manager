package com.example.newcalendar.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo") // 메모 테이블
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int, // 일련번호
    val content: String, // 내용
    val completion: Boolean // 체크 유무
)