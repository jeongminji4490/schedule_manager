package com.example.newcalendar.view.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.*
import com.example.newcalendar.alarm.AlarmFunctions
import com.example.newcalendar.databinding.AddScheduleDialogBinding
import com.example.newcalendar.model.entity.Alarm
import com.example.newcalendar.model.entity.Event
import com.example.newcalendar.model.entity.Schedule
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.newcalendar.viewmodel.*

class AddDialogFragment : DialogFragment(), View.OnClickListener { // 수정 다이얼로그

    private val binding by viewBinding(AddScheduleDialogBinding::bind)
    private val dateSaveModule : DateSaveModule by inject()
    private val scheduleViewModel : ScheduleViewModel by viewModel()
    private val alarmViewModel : AlarmViewModel by viewModel()
    private val eventViewModel : EventViewModel by viewModel()
    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }

    // 알람 데이터
    private lateinit var selectedDate : String // 선택된 날짜
    private var serialNum = 0 // 일련번호
    private var importance = 3 // 일정 중요도 (기본값 : 3 (어떤 항목도 선택되지 않았을 때) )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        return inflater.inflate(R.layout.add_schedule_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveScheduleBtn.setOnClickListener(this)
        binding.cancelDialogBtn.setOnClickListener(this)

        // 선택된 날짜 가져오기
        lifecycleScope.launch {
            selectedDate = dateSaveModule.date.first()
            binding.dateText.text = selectedDate
        }

        binding.timePicker.visibility = TimePicker.GONE // 타임피커 기본설정

        // 알람 온오프 클릭 listener
        binding.alarmOnOffBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){ // 알람 온
                binding.timePicker.visibility = TimePicker.VISIBLE
                binding.timePicker.setIs24HourView(true)
            }else{ // 알람 오프
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
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveScheduleBtn -> {
                val content = binding.content.text.toString()
                if (content.isEmpty() || importance==3){ //내용 비었을 때, 중요도 설정 안하면 저장 X
                    FancyToast.makeText(
                        context,
                        "내용 또는 중요도를 입력해주세요",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,
                        true).show()
                }else{
                    if (binding.alarmOnOffBtn.isChecked){ // alarm on
                        lifecycleScope.launch {
                            val hour = binding.timePicker.hour.toString()
                            val minute = binding.timePicker.minute.toString()
                            val alarm = "$selectedDate $hour:$minute:00"
                            val random = (1..100000) // 1~10000 범위에서 알람코드 랜덤으로 생성
                            val alarmCode = random.random()
                            setAlarm(alarmCode, content, alarm) // 알람 설정
                            withContext(Dispatchers.IO){
                                scheduleViewModel.addSchedule(
                                    Schedule(
                                        serialNum,
                                        selectedDate,
                                        content,
                                        alarm,
                                        hour,
                                        minute,
                                        alarmCode,
                                        importance)
                                )
                                eventViewModel.addDate(Event(selectedDate))
                                alarmViewModel.addAlarm(Alarm(alarmCode, alarm, content))
                            }
                        }
                    }else { // alarm off
                        lifecycleScope.launch {
                            val alarm = ""
                            val alarmCode = -1
                            withContext(Dispatchers.IO){
                                scheduleViewModel.addSchedule(
                                    Schedule(
                                        serialNum,
                                        selectedDate,
                                        content,
                                        alarm,
                                        "null",
                                        "null",
                                        alarmCode,
                                        importance)
                                )
                                eventViewModel.addDate(Event(selectedDate))
                            }
                        }
                    }
                    StyleableToast.makeText(requireContext(), "저장", R.style.saveToast).show()
                    this.dismiss()
                }
            }
            R.id.cancelDialogBtn -> {
                this.dismiss()
            }
        }
    }

    private fun setAlarm(alarmCode : Int, content : String, alarm : String){
        alarmFunctions.callAlarm(alarm, alarmCode, content)
    }

}