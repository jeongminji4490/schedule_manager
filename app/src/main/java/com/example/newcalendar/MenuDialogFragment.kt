package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.databinding.MenuDialogBinding
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

// 일정을 삭제 or 수정 할 수 있는 다이얼로그
class MenuDialogFragment()  : DialogFragment(), View.OnClickListener{

    var serialNum: Int = 0 // 일정 일련번호
    var alarmCode: Int = -1 // 일정 알람코드
    var selectedDate: String = "" // 선택된 날짜
    var size: Int = 0 // 리스트 사이즈

    private val binding by viewBinding(MenuDialogBinding::bind)
    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private var job : Job? = null
    private val viewModel : ViewModel by inject()

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
        if (id == R.id.deleteOkBtn){ // 삭제 (완료는 일정을 완전히 끝냈다는 뜻, 삭제랑 개념이 다름)
            job = lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    viewModel.deleteSchedule(serialNum)
                    viewModel.deleteAlarm(alarmCode)
                    if (size == 1){ // 일정이 하나만 남았다면
                        viewModel.deleteDate(selectedDate) // 도트 삭제
                    }
                }
            }
            alarmFunctions.cancelAlarm(alarmCode) // 알람 취소
            context?.let { StyleableToast.makeText(it, "삭제", R.style.deleteToast).show() }
            this.dismiss()
        }
        if (id == R.id.modifyBtn){ // 변경
            val dialog = ScheduleModifyFragment(serialNum)
            activity?.let {
                dialog.show(it.supportFragmentManager, "ShowListFragment")
            }
            this.dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        Log.e(TAG, "onStop()")
    }

    companion object{
        const val TAG = "MenuDialogFragment"
    }
}