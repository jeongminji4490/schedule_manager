package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.ScheduleListFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class ShowListFragment : DialogFragment(){ // 저장한 일정들의 목록을 보여주는 다이얼로그

    private lateinit var binding : ScheduleListFragmentBinding
    private lateinit var selectedDate : String
    private val dateSaveModule : DateSaveModule by inject()
    private val viewModel : ViewModel by inject()
    private val scope : CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }
    private val ioScope : CoroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScheduleListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val adapter = ScheduleAdapter(context)

        binding.scheduleListview.layoutManager=LinearLayoutManager(context)
        scope.launch {
            selectedDate = dateSaveModule.date.first()
            binding.dateText.text = selectedDate
        }

        adapter.itemClick = object : ScheduleAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<Schedule>) {
                val serialNum = list[position].rqCode
                val dialog = DeleteDialogFragment(serialNum)
                activity?.let {
                    dialog.show(it.supportFragmentManager, "ShowListFragment")
                }
            }
        }

        viewModel.getAllSchedule().observe(this, androidx.lifecycle.Observer {
            adapter.removeAll()
            for(i in it.indices){
                if (it[i].date == selectedDate){
                    val data = Schedule(
                        it[i].alarm_rqCode,
                        it[i].date,
                        it[i].content,
                        it[i].alarm,
                        it[i].importance)
                    adapter.addItems(data)
                }
            }
            binding.scheduleListview.adapter = adapter
        })
    }
}