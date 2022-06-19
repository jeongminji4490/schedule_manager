package com.example.newcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newcalendar.databinding.ScheduleListFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import kotlin.collections.ArrayList

class ShowListFragment : DialogFragment(){ // 저장한 일정들의 목록을 보여주는 다이얼로그

    private lateinit var binding : ScheduleListFragmentBinding
    private lateinit var selectedDate : String
    private val dateSaveModule : DateSaveModule by inject()
    private val viewModel : ViewModel by inject()
    private val scope : CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScheduleListFragmentBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_white_rounded_shape)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ScheduleAdapter(requireContext(), viewModel)

        lifecycleScope.launch {
            selectedDate = dateSaveModule.date.first()
            binding.dateText.text = selectedDate
        }

        adapter.itemClick = object : ScheduleAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<Schedule>) {
                val serialNum = list[position].serialNum
                val alarmCode = list[position].alarm_code
                val date = list[position].date
                val size = list.size
                val dialog = MenuDialogFragment(serialNum,alarmCode, date, size)
                activity?.let {
                    dialog.show(it.supportFragmentManager, "ShowListFragment")
                }
            }
        }

        // 선택된 날짜에 해당하는 일정 목록 가져오기
        viewModel.getAllSchedule(selectedDate).observe(this, androidx.lifecycle.Observer {
            adapter.removeAll()
            it.let {
                for(i in it.indices){
                    binding.noticeText.visibility = View.GONE
                    val data = Schedule(
                        it[i].serialNum,
                        it[i].date,
                        it[i].content,
                        it[i].alarm,
                        it[i].alarm_code,
                        it[i].importance)
                    adapter.addItems(data)
                }
            }
            binding.scheduleListview.adapter = adapter
            binding.scheduleListview.layoutManager=LinearLayoutManager(requireContext())
        })
    }
}