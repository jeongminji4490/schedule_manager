package com.example.newcalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newcalendar.databinding.AddScheduleDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddDialogFrament : DialogFragment() {

    private lateinit var binding : AddScheduleDialogBinding
    private lateinit var selectedDate : String
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
            selectedDate = App.getInstance().getDataStore().date.first()
            binding.dateText.text = selectedDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    companion object{
        const val TAG = "AddDialogFragment"
    }
}