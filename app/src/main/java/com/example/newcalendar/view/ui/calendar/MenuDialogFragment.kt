package com.example.newcalendar.view.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.*
import com.example.newcalendar.alarm.AlarmFunctions
import com.example.newcalendar.databinding.MenuDialogBinding
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.newcalendar.viewmodel.*

// 일정을 삭제 or 수정 할 수 있는 다이얼로그
class MenuDialogFragment()  : DialogFragment(), View.OnClickListener{

    var serialNum: Int = 0 // 일정 일련번호
    var alarmCode: Int = -1 // 일정 알람코드
    var selectedDate: String = "" // 선택된 날짜
    var size: Int = 0 // 리스트 사이즈

    private val binding by viewBinding(MenuDialogBinding::bind)
    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private val scheduleViewModel : ScheduleViewModel by viewModel()
    private val alarmViewModel : AlarmViewModel by viewModel()
    private val eventViewModel : EventViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteOkBtn.setOnClickListener(this)
        binding.modifyBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.deleteOkBtn){ // 일정 삭제
            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    scheduleViewModel.deleteSchedule(serialNum)
                    alarmViewModel.deleteAlarm(alarmCode)
                    if (size == 1){ // 일정이 하나만 남았다면
                        eventViewModel.deleteDate(selectedDate) // 도트 삭제
                    }
                }
            }
            alarmFunctions.cancelAlarm(alarmCode) // 알람 취소
            StyleableToast.makeText(requireContext(), "삭제", R.style.deleteToast).show()
            this.dismiss()
        }
        if (id == R.id.modifyBtn){ // 일정 변경 다이얼로그 호출
            val dialog = ScheduleModifyFragment(serialNum)
            activity?.let {
                dialog.show(it.supportFragmentManager, "ShowListFragment")
            }
            this.dismiss()
        }
    }
}