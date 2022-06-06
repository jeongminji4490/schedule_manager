package com.example.newcalendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newcalendar.databinding.DeleteDialogBinding
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.schedule_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DeleteDialogFragment()  : DialogFragment(), View.OnClickListener{

    //private var alarm_code = 0
    private var serialNum = 0
    private lateinit var content: String
    private lateinit var binding : DeleteDialogBinding
    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val viewModel : ViewModel by inject()

    constructor(serialNum: Int, content: String) : this() {
        this.serialNum = serialNum
        this.content = content
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = DeleteDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteOkBtn.setOnClickListener(this)
        binding.deleteCancelBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.deleteOkBtn){ // 삭제
            ioScope.launch {
                viewModel.deleteSchedule(serialNum)
            }
            alarmFunctions.cancelAlarm(viewModel, serialNum)
            //FancyToast.makeText(context,"delete", FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show()
            context?.let { StyleableToast.makeText(it, "삭제", R.style.deleteToast).show() }
            this.dismiss()
        }
        if (id == R.id.deleteCancelBtn){ // 다이얼로그 닫기
            this.dismiss()
        }
    }
}