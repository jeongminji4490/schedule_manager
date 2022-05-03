package com.example.newcalendar

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
import org.koin.android.ext.android.inject

class AddDialogFrament : DialogFragment() { // 수정 다이얼로그

    private lateinit var binding : AddScheduleDialogBinding
    private lateinit var selectedDate : String
    private lateinit var hour : String
    private lateinit var minute : String
    private val dateSaveModule : DateSaveModule by inject()
    private val scope by lazy { CoroutineScope(Dispatchers.Main) }

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
                hour = binding.timePicker.hour.toString()
                minute = binding.timePicker.minute.toString()
            }else{
                binding.timePicker.visibility = TimePicker.GONE
            }
        }

        binding.saveScheduleBtn.setOnClickListener {
            setAlarm(hour, minute)
        }
    }

    private fun setAlarm(hour : String, minute : String){
        Log.i(TAG, "$hour + $minute")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object{
        const val TAG = "AddDialogFragment"
    }
}