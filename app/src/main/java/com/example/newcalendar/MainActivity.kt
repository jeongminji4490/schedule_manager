package com.example.newcalendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.newcalendar.databinding.ActivityMainBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private val context by lazy { this }
    private lateinit var todayDate : String
    private lateinit var selectedDate: String
    private val dateSaveModule : DateSaveModule by inject()

    //private val dateFormat by lazy { SimpleDateFormat("yyyy-mm-dd") }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addScheduleBtn.setOnClickListener(this)
        binding.openScheduleBtn.setOnClickListener(this)

        binding.calendarView.dayBinder=object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view, context, dateSaveModule)

            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                //container.textView.text = day.date.dayOfMonth.toString()
                val sYear = day.date.year
                val sMonth = day.date.month
                val sDay = day.date.dayOfMonth
                container.year = sYear
                container.month = sMonth
                container.textView.text = sDay.toString()

                selectedDate = "$sYear-$sMonth-$sDay" //선택한 날짜

                if (selectedDate.equals(todayDate)){ //오늘 날짜 표시
                    container.textView.setBackgroundResource(R.drawable.today_background)
                }

                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.BLACK)
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
            @SuppressLint("SetTextI18n")
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    container.textView.text = "${
                        month.yearMonth.month.name.lowercase(Locale.getDefault())
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    } ${month.year}"
                }
            }
        }

        val currentMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            YearMonth.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addScheduleBtn -> {
                val dialog = AddDialogFragment()
                dialog.show(supportFragmentManager, "AddScheduleDialog")
            }
            R.id.openScheduleBtn -> {
                val dialog = ShowListFragment()
                dialog.show(supportFragmentManager, "ShowListFragment")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //오늘 날짜 미리 저장하기
        val tYear = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().year
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val tMonth = LocalDate.now().month
        val tMonthValue = tMonth.value
        val tDay = LocalDate.now().dayOfMonth

        todayDate = "$tYear-$tMonth-$tDay"
        val todayDateForSave = "$tYear-$tMonthValue-$tDay"
        CoroutineScope(Dispatchers.IO).launch {
            dateSaveModule.setDate(todayDateForSave)
        }
    }
}