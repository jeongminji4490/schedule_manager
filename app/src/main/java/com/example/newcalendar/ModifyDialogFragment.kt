package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.newcalendar.databinding.AddScheduleDialogBinding
import com.example.newcalendar.databinding.ModifyDialogBinding
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class ModifyDialogFragment() : DialogFragment() {

    private lateinit var binding : ModifyDialogBinding
    private val viewModel : ViewModel by inject()
    private lateinit var schedule : ScheduleDataModel
    private var getJob : Job? = null
    private var setJob : Job? = null

    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private var serialNum = -1
    private var alarmCode = -1
    private var importance = 3 // 일정 중요도

    constructor(serialNum: Int) : this() {
        this.serialNum = serialNum
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModifyDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_white_rounded_shape)
        return binding.root
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
                        val random = (1..100000) // 1~10000 범위에서 알람코드 랜덤으로 생성
                        val alarmCode = random.random()
                        withContext(Dispatchers.IO){
                            viewModel.addSchedule(ScheduleDataModel(serialNum, date, content, alarm, hour, minute, alarmCode, importance))
                            viewModel.addDate(EventDataModel(date))
                            viewModel.addAlarm(AlarmDataModel(alarmCode, alarm, content))
                        }
                        setAlarm(alarmCode, content, alarm)
                    }
                }else {
                    setJob = lifecycleScope.launch { // 알람 설정 안했을 때
                        val alarm = ""
                        withContext(Dispatchers.IO){
                            viewModel.addSchedule(ScheduleDataModel(serialNum, date, content, alarm, "null", "null", alarmCode, importance))
                        }
                    }
                }
                context?.let { StyleableToast.makeText(it, "저장", R.style.saveToast).show() }
                this.dismiss()
            }
        }
    }

    private fun setAlarm(alarmCode : Int, content : String, alarm : String){
        alarmFunctions.callAlarm(alarm, alarmCode, content)
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
        getJob?.cancel()
        setJob?.cancel()
        //Log.e("AddDialogFragment", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        //Log.e("AddDialogFragment", "onStop()")
    }
}