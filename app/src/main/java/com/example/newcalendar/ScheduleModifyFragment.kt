package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.databinding.ModifyScheduleDialogBinding
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class ScheduleModifyFragment(
    private var serialNum: Int
) : DialogFragment() {

    private val binding by viewBinding(ModifyScheduleDialogBinding::bind)
    private val viewModel : ViewModel by inject()
    private lateinit var schedule : ScheduleDataModel
    private var getJob : Job? = null
    private var setJob : Job? = null

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

        // 기존에 설정헀던 알람이 있다면 취소하고 재설정
        // 만약 기존에 설정했던 알람이 없다면 알람코드의 값은 -1
        // 기존에 설정했던 알람을 취소한다면 cancelAlarm 호출
        binding.saveScheduleBtn.setOnClickListener {
            val date = binding.date.text.toString()
            val content = binding.content.text.toString()
            if (content.isEmpty() || importance==3){ //내용 비었을 때, 중요도 설정 안하면 저장 X
                FancyToast.makeText(context,"내용 또는 중요도를 입력해주세요",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,true).show()
            }else{ // 알람 설정했을 때
                if (binding.alarmOnOffBtn.isChecked){ // alarm on
                    setJob = lifecycleScope.launch {
                        val hour = binding.timePicker.hour.toString()
                        val minute = binding.timePicker.minute.toString()
                        val alarm = "$date $hour:$minute:00"
                        if (alarmCode != 1){ // 기존에 설정된 알림이 있다면 취소하고 재설정
                            cancelAlarm(alarmCode)
                            withContext(Dispatchers.IO){
                                viewModel.deleteAlarm(alarmCode)
                            }
                        }
                        val random = (1..100000) // 1~10000 범위에서 알람코드 랜덤으로 생성
                        val alarmCode = random.random()
                        setAlarm(alarmCode, content, alarm)
                        withContext(Dispatchers.IO){
                            viewModel.addSchedule(ScheduleDataModel(serialNum, date, content, alarm, hour, minute, alarmCode, importance))
                            viewModel.addDate(EventDataModel(date))
                            viewModel.addAlarm(AlarmDataModel(alarmCode, alarm, content))
                        }
                    }
                }else {
                    setJob = lifecycleScope.launch { // 알람 설정 안했을 때 알람 취소해야함!
                        val alarm = ""
                        withContext(Dispatchers.IO){
                            viewModel.addSchedule(ScheduleDataModel(serialNum, date, content, alarm, "null", "null", -1, importance))
                        }
                        if (alarmCode != -1){
                            cancelAlarm(alarmCode)
                            withContext(Dispatchers.IO){
                                viewModel.deleteAlarm(alarmCode)
                            }
                        }
                    }
                }
                context?.let { StyleableToast.makeText(it, "저장", R.style.saveToast).show() }
                this.dismiss()
            }
        }
    }

    private fun setAlarm(alarmCode : Int, content : String, alarm : String){
        Log.e(TAG, "call alarm $alarmCode")
        alarmFunctions.callAlarm(alarm, alarmCode, content)
    }

    private fun cancelAlarm(alarmCode: Int){
        Log.e(TAG, "cancel alarm $alarmCode")
        alarmFunctions.cancelAlarm(alarmCode)
    }

    override fun onStart() {
        super.onStart()
        // 기존 내용으로 일정 변경화면 세팅
        getJob = lifecycleScope.launch() {
            withContext(Dispatchers.IO) {
                schedule = viewModel.getSchedule(serialNum) // 일정 내용 가져오기
            }
            binding.date.text = schedule.date
            binding.content.setText(schedule.content)
            if (schedule.hour!="null"){ // 알람 시간이 null 아니면 타임피커 셋팅
                binding.timePicker.visibility = View.VISIBLE
                binding.timePicker.setIs24HourView(true)
                binding.timePicker.hour = schedule.hour.toInt()
                binding.timePicker.minute = schedule.minute.toInt()
                binding.alarmOnOffBtn.isChecked = true
                alarmCode = schedule.alarm_code // 기존의 알람코드
            }else{
                binding.timePicker.visibility = View.GONE
                binding.alarmOnOffBtn.isChecked = false
            }
            when(schedule.importance){
                0 -> { binding.veryBtn.isChecked = true }
                1 -> { binding.middleBtn.isChecked = true }
                2 -> { binding.lessBtn.isChecked = true }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("AddDialogFragment", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        getJob?.cancel()
        setJob?.cancel()
        Log.e(TAG, "onStop()")
    }

    companion object{
        const val TAG = "AddDialogFragment"
    }
}