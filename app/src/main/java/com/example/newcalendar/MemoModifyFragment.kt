package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.databinding.ModifyMemoDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MemoModifyFragment(
    private val content: String,
    private val serialNum : Int,
) : DialogFragment() {

    private val binding by viewBinding(ModifyMemoDialogBinding::bind,
    onViewDestroyed = {
        job?.cancel()
    })

    private var job : Job? = null
    private val viewModel : ViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contentEdit.setText(content)
        binding.modifyBtn.setOnClickListener {
            val newContent = binding.contentEdit.text.toString()
            job = lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    viewModel.changeContent(newContent, serialNum)
                }
            }
            this.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("memoFrag", "onDestroyView")
        //job?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("memoFrag", "onDestroy")
    }
}