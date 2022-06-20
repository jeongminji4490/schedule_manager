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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first

class CalendarFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentCalendarBinding
    private lateinit var selectedDate: String
    private val dateSaveModule : DateSaveModule by inject()
    private val viewModel : ViewModel by inject()
    private var setJob : Job? = null

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

        val adapter = ScheduleAdapter(requireContext(), viewModel)

        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.addDecorators(SaturdayDecorator(), SundayDecorator())

        var year = binding.calendarView.selectedDate!!.year
        var month = binding.calendarView.selectedDate!!.month + 1
        var day = binding.calendarView.selectedDate!!.day
        selectedDate = "$year-$month-$day"
        Log.e(TAG, selectedDate)
        setJob = lifecycleScope.launch {
            dateSaveModule.setDate(selectedDate)
        }

        binding.addScheduleBtn.setOnClickListener(this)
        binding.openScheduleBtn.setOnClickListener(this)

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            year = binding.calendarView.selectedDate!!.year
            month = binding.calendarView.selectedDate!!.month + 1
            day = binding.calendarView.selectedDate!!.day
            val selectedDate = "$year-$month-$day"
            setJob = lifecycleScope.launch {
                dateSaveModule.setDate(selectedDate)
            }
            // 선택된 날짜에 해당하는 일정 목록 가져오기
            viewModel.getAllSchedule(selectedDate).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                adapter.removeAll()
                if (it.isEmpty()){
                    binding.emptyText.visibility = View.VISIBLE
                }else{
                    for(i in it.indices){
                        binding.emptyText.visibility = View.GONE
                        val data = Schedule(
                            it[i].serialNum,
                            it[i].date,
                            it[i].content,
                            it[i].alarm,
                            it[i].alarm_code,
                            it[i].importance)
                        adapter.addItems(data)
                    }
                }
                binding.scheduleListview.adapter = adapter
                binding.scheduleListview.layoutManager=LinearLayoutManager(requireContext())
            })
            Log.e(TAG, selectedDate)
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
                            Color.parseColor("#0E406B"),
                            Collections.singleton(CalendarDay.from(year, month-1, day))))
            }
        })

        adapter.itemClick = object : ScheduleAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<Schedule>) {
                val serialNum = list[position].serialNum
                val alarmCode = list[position].alarm_code
                val date = list[position].date
                val size = list.size
                val dialog = MenuDialogFragment(serialNum,alarmCode, date, size)
                activity?.let {
                    dialog.show(it.supportFragmentManager, "ShowListFragment")
                }
            }
        }

        // 선택된 날짜에 해당하는 일정 목록 가져오기
        viewModel.getAllSchedule(selectedDate).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.removeAll()
            if (it.isEmpty()){
                binding.emptyText.visibility = View.VISIBLE
            }else{
                for(i in it.indices){
                    binding.emptyText.visibility = View.GONE
                    val data = Schedule(
                        it[i].serialNum,
                        it[i].date,
                        it[i].content,
                        it[i].alarm,
                        it[i].alarm_code,
                        it[i].importance)
                    adapter.addItems(data)
                }
            }
            binding.scheduleListview.adapter = adapter
            binding.scheduleListview.layoutManager=LinearLayoutManager(requireContext())
        })
        Log.e(TAG, selectedDate)
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
        binding.calendarView.selectedDate = CalendarDay.today()
        Log.e(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        binding.calendarView.selectedDate = CalendarDay.today()
        Log.e(TAG, "onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy()")
    }

    companion object{
        const val TAG = "CalendarFragment"
    }
}