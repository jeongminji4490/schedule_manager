package com.example.newcalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.newcalendar.databinding.AddScheduleDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.koin.android.ext.android.inject

class AddDialogFrament : DialogFragment(), View.OnClickListener { // 수정 다이얼로그

    private lateinit var binding : AddScheduleDialogBinding
    private val dateSaveModule : DateSaveModule by inject()
    private val scope by lazy { CoroutineScope(Dispatchers.Main) }
    private lateinit var hour : String
    private lateinit var minute : String

    //db
    //날짜 어떻게?
    private var serialNum = 0
    private lateinit var selectedDate : String
    private lateinit var content : String
    private lateinit var alarm : String  //2000-00-00 hh:mm:ss
    private var rqCode = 0
    private var importance = 0

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
            binding.dateText.text = selectedDate
        }

        binding.timePicker.visibility = TimePicker.GONE // 타임피커 기본설정

        //dynamic timepicker
        binding.alarmOnOffBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                binding.timePicker.visibility = TimePicker.VISIBLE
                binding.timePicker.setIs24HourView(true)
            }else{
                binding.timePicker.visibility = TimePicker.GONE
            }
        }

        binding.saveScheduleBtn.setOnClickListener(this)

//        binding.saveScheduleBtn.setOnClickListener {
//            hour = binding.timePicker.hour.toString()
//            minute = binding.timePicker.minute.toString()
//            setAlarm(hour, minute)
//        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveScheduleBtn -> {
                hour = binding.timePicker.hour.toString()
                minute = binding.timePicker.minute.toString()
                alarm = "$selectedDate $hour:$minute:00"
                content = binding.content.text.toString()

                binding.radioGroup.setOnCheckedChangeListener { radioGroup, id ->
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

                setAlarm(alarm)
            }
        }
    }

    private fun setAlarm(alarm : String){
        Log.i(TAG, alarm)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object{
        const val TAG = "AddDialogFragment"
    }
}