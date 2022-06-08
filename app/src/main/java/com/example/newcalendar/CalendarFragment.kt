package com.example.newcalendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.newcalendar.databinding.FragmentCalendarBinding
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
import kotlin.collections.ArrayList

class CalendarFragment : Fragment(), View.OnClickListener{

    private lateinit var binding : FragmentCalendarBinding
    private lateinit var todayDate : String
    private lateinit var selectedDate: String
    private val dateSaveModule : DateSaveModule by inject()
    private val viewModel : ViewModel by inject()
    private var list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addScheduleBtn.setOnClickListener(this)
        binding.openScheduleBtn.setOnClickListener(this)

        // 일정이 저장된 특정 날짜들을 가져와서 리스트에 넣기
        viewModel.getAllDates().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            for (i in it.indices){
                list.add(it[i].date)
                Log.e("Main", it[i].date)
            }
        })

        binding.calendarView.dayBinder=object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(dateSaveModule, view)

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

                for (i in list.indices){
                    if (selectedDate == list[i]){
                        container.textView.setBackgroundResource(R.drawable.event) // 일정이 저장된 날짜만 별표시
                    }
                }
                //Log.e("selectedDate", selectedDate)

                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.BLACK)
                    if (selectedDate == todayDate){ //오늘 날짜만 원으로 표시
                        container.textView.setBackgroundResource(R.drawable.selected_background)
                        container.textView.setTextColor(Color.WHITE)
                    }
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
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
            dateSaveModule.setEvent(todayDate) // 일정이 저장된 날짜에만 별 아이콘을 표시하기 위해 영문으로도 저장해야함
        }
        CoroutineScope(Dispatchers.IO).launch {
            dateSaveModule.setDate(todayDateForSave)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addScheduleBtn -> {
                val dialog = AddDialogFragment()
                dialog.show(parentFragmentManager, "AddScheduleDialog")
            }
            R.id.openScheduleBtn -> {
                val dialog = ShowListFragment()
                dialog.show(parentFragmentManager, "ShowListFragment")
            }
        }
    }
}