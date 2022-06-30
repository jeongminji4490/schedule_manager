package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.databinding.FragmentCalendarBinding
import kotlinx.coroutines.Job

class CalendarFragment : Fragment(R.layout.fragment_calendar), View.OnClickListener {

    private val binding by viewBinding(FragmentCalendarBinding::bind,
    onViewDestroyed = { binding ->
        binding.scheduleListview.adapter = null // free view binding
    })

    private lateinit var selectedDate: String // 달력에서 선택한 날짜
    private val adapter by lazy { ScheduleAdapter() }
    private val dateSaveModule : DateSaveModule by inject() // 날짜를 저장하는 DataStore
    private val viewModel : ViewModel by inject()
    private var setJob : Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.selectedDate = CalendarDay.today() // 오늘 날짜 출력
        binding.calendarView.addDecorators(SaturdayDecorator(), SundayDecorator()) // 주말 강조

        var year = binding.calendarView.selectedDate!!.year
        var month = binding.calendarView.selectedDate!!.month + 1
        var day = binding.calendarView.selectedDate!!.day
        selectedDate = "$year-$month-$day"

        setJob = lifecycleScope.launch {
            dateSaveModule.setDate(selectedDate)
        }

        binding.addScheduleBtn.setOnClickListener(this)

        // 달력 - 날짜 선택 Listener
        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            year = date.year
            month = date.month + 1
            day = date.day
            selectedDate = "$year-$month-$day"
            setJob = lifecycleScope.launch {
                dateSaveModule.setDate(selectedDate)
            }
            callList()
        }

        // 일정 있는 날짜에 도트 찍기
        viewModel.getAllDates().observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            for (i in list.indices){
                val eventDate = list[i].date.split("-")
                val year = Integer.parseInt(eventDate[0])
                val month = Integer.parseInt(eventDate[1])
                val day = Integer.parseInt(eventDate[2])
                binding.calendarView
                    .addDecorator(
                        EventDecorator(
                            Color.parseColor("#0E406B"),
                            Collections.singleton(CalendarDay.from(year, month-1, day))))
            }
        })

        // recyclerview item click -> open MenuDialog
        adapter.itemClick = object : ScheduleAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<ScheduleDataModel>) {
                val dialog = MenuDialogFragment().apply {
                    serialNum = list[position].serialNum
                    alarmCode = list[position].alarm_code
                    selectedDate = list[position].date
                    size = list.size
                }
                activity?.let {
                    dialog.show(it.supportFragmentManager, "MenuDialogFragment")
                }
            }
        }
        callList()
    }

    // 선택된 날짜에 해당하는 일정 목록 가져오기
    private fun callList(){
        viewModel.getAllSchedule(selectedDate).observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            adapter.removeAll()
            if (list.isEmpty()){ // 해당 날짜에 목록이 없을 때 "이벤트 없음" 표시
                binding.emptyText.visibility = View.VISIBLE
            }else{
                binding.emptyText.visibility = View.GONE
                adapter.list = list as ArrayList<ScheduleDataModel>
            }
            binding.scheduleListview.adapter = adapter
            binding.scheduleListview.layoutManager=LinearLayoutManager(requireContext())
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addScheduleBtn -> { // open AddDialog
                val dialog = AddDialogFragment()
                dialog.show(parentFragmentManager, "AddScheduleDialog")
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
        setJob?.cancel()
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