package com.example.newcalendar

data class Schedule( // schedule_Listview 데이터클래스
    val serialNum : Int, //알람일련번호
    val date: String, //날짜
    val content : String, //내용
    val alarm : String, //알람시간
    val alarm_code : Int, //알람요청코드
    val importance : Int //중요도
)

data class Memo(
    val serialNum: Int,
    val content: String,
    val completion : Boolean
)

