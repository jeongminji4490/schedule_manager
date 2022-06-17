package com.example.newcalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.newcalendar.databinding.MenuDialogBinding
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.schedule_item.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

// 일정을 삭제 or 수정 할 수 있는 다이얼로그
class MenuDialogFragment()  : DialogFragment(), View.OnClickListener{

    private lateinit var binding : MenuDialogBinding
    private var serialNum = -1
    private var alarmCode = -1
    private var size = -1
    private lateinit var selectedDate : String
    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private var job : Job? = null
    private val viewModel : ViewModel by inject()

    constructor(serialNum: Int, code: Int, date: String, size: Int) : this() {
        this.serialNum = serialNum
        this.alarmCode = code
        this.selectedDate = date
        this.size = size
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = MenuDialogBinding.inflate(inflater)
        return binding.root
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
                    Log.e("Delete", size.toString())
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
            val dialog = ModifyDialogFragment(serialNum)
            activity?.let {
                dialog.show(it.supportFragmentManager, "ShowListFragment")
            }
            this.dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }
}