package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newcalendar.databinding.FragmentCalendarBinding
import kotlinx.coroutines.flow.first

class CalendarFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentCalendarBinding
    private lateinit var selectedDate: String
    private val dateSaveModule : DateSaveModule by inject()
    private val viewModel : ViewModel by inject()

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

        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.addDecorators(SaturdayDecorator(), SundayDecorator())

        var year = binding.calendarView.selectedDate!!.year
        var month = binding.calendarView.selectedDate!!.month + 1
        var day = binding.calendarView.selectedDate!!.day
        selectedDate = "$year-$month-$day"
        Log.e(TAG, selectedDate)
        lifecycleScope.launch {
            dateSaveModule.setDate(selectedDate)
        }
        viewModel.getCount(selectedDate).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.completionCount.text = it.toString()
            lifecycleScope.launch {
                dateSaveModule.setCount(it.toString())
                binding.sumCount.text = dateSaveModule.dataCount.first()
            }
        })

        binding.addScheduleBtn.setOnClickListener(this)
        binding.openScheduleBtn.setOnClickListener(this)

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            year = binding.calendarView.selectedDate!!.year
            month = binding.calendarView.selectedDate!!.month + 1
            day = binding.calendarView.selectedDate!!.day
            val selectedDate = "$year-$month-$day"
            lifecycleScope.launch {
                dateSaveModule.setDate(selectedDate)
            }
            Log.e(TAG, selectedDate)

            viewModel.getCount(selectedDate).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                binding.sumCount.text = it.toString()
            })
        }

        // 일정 있는 날짜에 도트 찍기
        viewModel.getAllDates().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            for (i in it.indices){
                val eventDate = it[i].date.split("-")
                val year = Integer.parseInt(eventDate[0])
                val month = Integer.parseInt(eventDate[1])
                val day = Integer.parseInt(eventDate[2])
                binding
                    .calendarView
                    .addDecorator(
                        EventDecorator(
                            Color.parseColor("#BE89E3"),
                            Collections.singleton(CalendarDay.from(year, month-1, day))))
            }
        })
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
        Log.e(TAG, "onStart()")
    }

    companion object{
        const val TAG = "RoutineFragment"
    }
}