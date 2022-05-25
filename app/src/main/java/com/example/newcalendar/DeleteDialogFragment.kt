package com.example.newcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newcalendar.databinding.DeleteDialogBinding
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DeleteDialogFragment(private val serialNum : Int)  : DialogFragment(), View.OnClickListener{

    private lateinit var binding : DeleteDialogBinding
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val viewModel : ViewModel by inject()

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
            FancyToast.makeText(context,"delete", FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show()
            this.dismiss()
        }
        if (id == R.id.deleteCancelBtn){ // 다이얼로그 닫기
            this.dismiss()
        }
    }
}