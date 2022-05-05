package com.example.newcalendar

data class Schedule(
    val serialNum : Int, //일련번호
    val date: String, //날짜
    val content : String, //내용
    val alarm : String, //알람시간
    val rqCode : Int, //알람요청번호
    val importance : Int //중요도
)

