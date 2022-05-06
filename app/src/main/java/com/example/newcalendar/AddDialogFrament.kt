package com.example.newcalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }
    private lateinit var hour : String
    private lateinit var minute : String

    //db
    //날짜 어떻게?
    private lateinit var selectedDate : String
    private lateinit var content : String
    private lateinit var alarm : String  //2000-00-00 hh:mm:ss
    private var rqCode = 0
    private var importance = 0
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
            binding.dateText.text = selectedDate
        }

        binding.timePicker.visibility = TimePicker.GONE // 타임피커 기본설정

        binding.alarmOnOffBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                binding.timePicker.visibility = TimePicker.VISIBLE
                binding.timePicker.setIs24HourView(true)
            }else{
                binding.timePicker.visibility = TimePicker.GONE
            }
        }

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
        binding.saveScheduleBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveScheduleBtn -> {
                content = binding.content.text.toString()
                if (binding.alarmOnOffBtn.isChecked){
                    ioScope.launch {
                        hour = binding.timePicker.hour.toString()
                        minute = binding.timePicker.minute.toString()
                        alarm = "$selectedDate $hour:$minute:00"
                        viewModel.sDao.addItem(ScheduleDataModel(rqCode, selectedDate, content, alarm, importance))
                    }
                }else {
                    ioScope.launch {
                        alarm = "null"
                        viewModel.sDao.addItem(ScheduleDataModel(rqCode, selectedDate, content, alarm, importance))
                    }
                }
                //setAlarm(alarm)
            }
        }
    }

//    private fun setAlarm(alarm : String){
//        Log.i(TAG, "$alarm $importance")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object{
        const val TAG = "AddDialogFragment"
    }

}