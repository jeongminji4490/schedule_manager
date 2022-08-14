package com.example.newcalendar.view.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.*
import com.example.newcalendar.alarm.AlarmFunctions
import com.example.newcalendar.databinding.ModifyScheduleDialogBinding
import com.example.newcalendar.model.entity.Alarm
import com.example.newcalendar.model.entity.Event
import com.example.newcalendar.model.entity.Schedule
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.newcalendar.viewmodel.*

class ScheduleModifyFragment(
    private var serialNum: Int // 일정 일련번호
) : DialogFragment() {

    private val binding by viewBinding(ModifyScheduleDialogBinding::bind)
    private val scheduleViewModel : ScheduleViewModel by viewModel()
    private val alarmViewModel : AlarmViewModel by viewModel()
    private val eventViewModel : EventViewModel by viewModel()
    private lateinit var schedule : Schedule

    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private var alarmCode = -1 // 알람 코드, 기본값 : -1 (기존에 알람이 설정되지 않았을 때)
    private var importance = 3 // 일정 중요도, 기본값 : 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.modify_schedule_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 체크박스 클릭 시 변경
        binding.alarmOnOffBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.timePicker.visibility = TimePicker.VISIBLE
                binding.timePicker.setIs24HourView(true)
            }else{
                binding.timePicker.visibility = TimePicker.GONE
            }
        }

        // 일정 중요도 설정
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            when(id){
                R.id.veryBtn -> {
                    importance = Importance.VERY.ordinal
                }
                R.id.middleBtn -> {
                    importance = Importance.MIDDLE.ordinal
                }
                R.id.lessBtn -> {
                    importance = Importance.LEAST.ordinal
                }
            }
        }

        binding.saveScheduleBtn.setOnClickListener {
            val date = binding.date.text.toString() // 날짜
            val content = binding.content.text.toString() // 내용
            if (content.isEmpty() || importance==3){ //내용 비었을 때 or 중요도 설정 안하면 저장 X
                FancyToast.makeText(context,"내용 또는 중요도를 입력해주세요",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,true).show()
            }else{
                if (binding.alarmOnOffBtn.isChecked){ // alarm on
                      lifecycleScope.launch {
                        // 날짜 시:분:00 형태로 알람시간 설정
                        val hour = binding.timePicker.hour.toString()
                        val minute = binding.timePicker.minute.toString()
                        val alarm = "$date $hour:$minute:00"
                        if (alarmCode != -1){ // 기존에 설정된 알림이 있다면 취소하고 재설정
                            cancelAlarm(alarmCode) // 알람 취소
                            withContext(Dispatchers.IO){
                                alarmViewModel.deleteAlarm(alarmCode) // 테이블에서 알람코드 삭제
                            }
                        }
                        val random = (1..100000) // 1~10000 범위에서 알람코드 랜덤으로 생성
                        val alarmCode = random.random()
                        setAlarm(alarmCode, content, alarm) // 알람 설정
                        withContext(Dispatchers.IO){
                            scheduleViewModel.addSchedule(
                                Schedule(
                                    serialNum,
                                    date,
                                    content,
                                    alarm,
                                    hour,
                                    minute,
                                    alarmCode,
                                    importance)
                            )
                            eventViewModel.addDate(Event(date))
                            alarmViewModel.addAlarm(Alarm(alarmCode, alarm, content))
                        }
                    }
                }else { // alarm off
                    lifecycleScope.launch {
                        val alarm = ""
                        withContext(Dispatchers.IO){
                            scheduleViewModel.addSchedule(
                                Schedule(
                                    serialNum,
                                    date,
                                    content,
                                    alarm,
                                    "null",
                                    "null",
                                    -1,
                                    importance)
                            )
                        }
                        if (alarmCode != -1){ // 기존에 설정된 알림이 있다면 취소
                            cancelAlarm(alarmCode) // 알람 취소
                            withContext(Dispatchers.IO){
                                alarmViewModel.deleteAlarm(alarmCode) // 테이블에서 삭제
                            }
                        }
                    }
                }
                StyleableToast.makeText(requireContext(), "저장", R.style.saveToast).show()
                this.dismiss()
            }
        }
    }

    private fun setAlarm(alarmCode : Int, content : String, alarm : String){
        alarmFunctions.callAlarm(alarm, alarmCode, content)
    }

    private fun cancelAlarm(alarmCode: Int){
        alarmFunctions.cancelAlarm(alarmCode)
    }

    override fun onStart() {
        super.onStart()
        // 기존 내용으로 일정 변경화면 세팅
        lifecycleScope.launch() {
            withContext(Dispatchers.IO) {
                schedule = scheduleViewModel.getSchedule(serialNum) // 일정 내용 가져오기
            }
            if (schedule.hour!="null"){ // 알람 시간이 null 아니면 타임피커 셋팅
                with(binding) {
                    date.text = schedule.date
                    content.setText(schedule.content)
                    timePicker.visibility = View.VISIBLE
                    timePicker.setIs24HourView(true)
                    timePicker.hour = schedule.hour.toInt()
                    timePicker.minute = schedule.minute.toInt()
                    alarmOnOffBtn.isChecked = true
                }
                alarmCode = schedule.alarm_code // 기존의 알람코드
            }else{
                with(binding){
                    date.text = schedule.date
                    content.setText(schedule.content)
                    timePicker.visibility = View.GONE
                    alarmOnOffBtn.isChecked = false
                }
            }
            when(schedule.importance){
                0 -> { binding.veryBtn.isChecked = true }
                1 -> { binding.middleBtn.isChecked = true }
                2 -> { binding.lessBtn.isChecked = true }
            }
        }
    }

    companion object{
        const val TAG = "AddDialogFragment"
    }
}