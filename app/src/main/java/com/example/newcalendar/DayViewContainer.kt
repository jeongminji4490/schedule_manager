package com.example.newcalendar

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Month
import java.util.*

class DayViewContainer(view: View, context: Context) : ViewContainer(view) {

    val textView: TextView =view.findViewById<TextView>(R.id.calendarDayText)
    var year = 0
    lateinit var month : Month
    lateinit var day : String
    lateinit var date : String
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    init {
        textView.setOnClickListener {
            if (textView.currentTextColor == -1){ //두번째 클릭했을 때 원래대로
                textView.setBackgroundResource(R.drawable.canceled_background)
                textView.setTextColor(Color.BLACK)
            }else{
                textView.setBackgroundResource(R.drawable.selected_background)
                textView.setTextColor(Color.WHITE)
                day = textView.text.toString()
                date = "$year $month $day"

                coroutineScope.launch { //날짜 저장
                    App.getInstance().getDataStore().setDate(date)
                    Log.e("DayViewContainer2", date)
                }
            }
        }
    }

}