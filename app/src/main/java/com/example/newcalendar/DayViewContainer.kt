package com.example.newcalendar

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.time.Month

class DayViewContainer(module: DateSaveModule, view: View) : ViewContainer(view) {

    val textView: TextView =view.findViewById<TextView>(R.id.calendarDayText)
    lateinit var month :Month
    var monthValue = 0
    var year = 0
    lateinit var day : String
    lateinit var date : String
    lateinit var event : String
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    init {
        textView.setOnClickListener {
            if (textView.currentTextColor == -1){ //두번째 클릭했을 때 원래대로
                textView.setBackgroundResource(R.drawable.event)
//                textView.setTextColor(Color.BLACK)
            }else{
                textView.setBackgroundResource(R.drawable.select_star)
//                textView.setTextColor(Color.WHITE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    monthValue = month.value
                    //Log.e("Day", month.toString())
                }
                day = textView.text.toString()
                date = "$year-$monthValue-$day"
                event = "$year-$month-$day"

                coroutineScope.launch { //날짜 저장
                    module.setDate(date)
                    module.setEvent(event)
                }
            }
        }
    }

}