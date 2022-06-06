package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.newcalendar.databinding.AddScheduleDialogBinding
import com.shashank.sony.fancytoastlib.FancyToast
import es.dmoral.toasty.Toasty
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class AddDialogFragment : DialogFragment(), View.OnClickListener { // 수정 다이얼로그

    private lateinit var binding : AddScheduleDialogBinding
    private val dateSaveModule : DateSaveModule by inject()
    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private val scope by lazy { CoroutineScope(Dispatchers.Main) }
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }
    private lateinit var hour : String
    private lateinit var minute : String

    private lateinit var selectedDate : String
    private lateinit var eventDate : String

    private lateinit var content : String
    private lateinit var alarm : String  //2000-00-00 hh:mm:ss
    private var serialNum = 0 // 일련번호
    private var alarm_code = 0 // 알람요청코드
    private var importance = 3 // 일정 중요도
    private val viewModel : ViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddScheduleDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope.launch {
            selectedDate = dateSaveModule.date.first()
            eventDate = dateSaveModule.event.first()
            binding.dateText.text = selectedDate
        }

        binding.timePicker.visibility = TimePicker.GONE // 타임피커 기본설정

        binding.alarmOnOffBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.timePicker.visibility = TimePicker.VISIBLE
                binding.timePicker.setIs24HourView(true)
            }else{
                binding.timePicker.visibility = TimePicker.GONE
            }
        }

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
        binding.saveScheduleBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveScheduleBtn -> {
                content = binding.content.text.toString()
                if (content.isEmpty() || importance==3){ //내용 비었을 때, 중요도 설정 안하면 저장 X
                    FancyToast.makeText(context,"내용 또는 중요도를 입력해주세요",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show()
                }else{
                    if (binding.alarmOnOffBtn.isChecked){ // alarm on
                        ioScope.launch {
                            hour = binding.timePicker.hour.toString()
                            minute = binding.timePicker.minute.toString()
                            alarm = "$selectedDate $hour:$minute:00"
                            val random = (1..100000) // 1~10000 범위에서 알람코드 랜덤으로 생성
                            alarm_code = random.random()
                            viewModel.addSchedule(ScheduleDataModel(serialNum, selectedDate, content, alarm, alarm_code, importance))
                            viewModel.addDate(EventDataModel(eventDate))
                            setAlarm(alarm_code, content, alarm)
                        }
                    }else {
                        ioScope.launch {
                            alarm = "null"
                            viewModel.addSchedule(ScheduleDataModel(serialNum, selectedDate, content, alarm, alarm_code, importance))
                            viewModel.addDate(EventDataModel(eventDate))
                        }
                    }
                    context?.let { StyleableToast.makeText(it, "저장", R.style.saveToast).show() }
                    this.dismiss()
                }
            }
        }
    }

    private fun setAlarm(alarm_code : Int, content : String, alarm : String){
        alarmFunctions.callAlarm(alarm, alarm_code, content)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object{
        const val TAG = "AddDialogFragment"
    }

}