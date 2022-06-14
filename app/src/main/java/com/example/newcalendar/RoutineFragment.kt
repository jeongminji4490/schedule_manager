package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.newcalendar.databinding.FragmentRoutineBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Color

class RoutineFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentRoutineBinding
    private lateinit var todayDate : String
    private lateinit var selectedDate: String
    private val dateSaveModule : DateSaveModule by inject()
    private val viewModel : ViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoutineBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialCalendarView.selectedDate = CalendarDay.today()
        binding.materialCalendarView.addDecorators(SaturdayDecorator(), SundayDecorator())

        var year = binding.materialCalendarView.selectedDate!!.year
        var month = binding.materialCalendarView.selectedDate!!.month + 1
        var day = binding.materialCalendarView.selectedDate!!.day
        selectedDate = "$year-$month-$day"
        lifecycleScope.launch {
            dateSaveModule.setDate(selectedDate)
        }

        binding.addScheduleBtn.setOnClickListener(this)
        binding.openScheduleBtn.setOnClickListener(this)

        binding.materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            year = binding.materialCalendarView.selectedDate!!.year
            month = binding.materialCalendarView.selectedDate!!.month + 1
            day = binding.materialCalendarView.selectedDate!!.day
            val selectedDate = "$year-$month-$day"
            lifecycleScope.launch {
                dateSaveModule.setDate(selectedDate)
            }
            Log.e(TAG, selectedDate)
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

    override fun onStart() {
        super.onStart()
        // 일정이 저장된 특정 날짜들을 가져와서 리스트에 넣기
        viewModel.getAllDates().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            for (i in it.indices){
                val eventDate = it[i].date.split("-")
                val year = Integer.parseInt(eventDate[0])
                val month = Integer.parseInt(eventDate[1])
                val day = Integer.parseInt(eventDate[2])
                Log.e(TAG, year.toString())
                Log.e(TAG, month.toString())
                Log.e(TAG, day.toString())
                binding.materialCalendarView.addDecorator(EventDecorator(Color.parseColor("#EE82EE"),Collections.singleton(CalendarDay.from(year, month-1, day))))
            }
        })
    }

    companion object{
        const val TAG = "RoutineFragment"
    }
}