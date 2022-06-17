package com.example.newcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newcalendar.databinding.FragmentMemoBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MemoFragment : Fragment() {

    private lateinit var binding : FragmentMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serialNum = 0
        val viewModel : ViewModel by inject()
        val adapter by lazy { MemoAdapter(requireContext(), viewModel) }

        val month =  CalendarDay.today().month.toString()
        val day = CalendarDay.today().day.toString()
        val date = "${month}월 ${day}일"

        binding.todayDate.text = date

        binding.saveBtn.setOnClickListener {
            val memo = binding.memoEdit.text.toString()
            val completion = false
            if (memo.isNotEmpty()){
                lifecycleScope.launch {
                    withContext(Dispatchers.IO){
                        viewModel.addMemo(MemoDataModel(serialNum, memo, completion))
                    }
                }
            }
        }

        viewModel.getAllMemo().observe(viewLifecycleOwner, Observer {
            adapter.removeAll()
            for (i in it.indices){
                val item = MemoDataModel(
                    it[i].serialNum,
                    it[i].content,
                    it[i].completion)
                adapter.addItem(item)
            }
            binding.todoListView.adapter = adapter
            binding.todoListView.layoutManager = LinearLayoutManager(requireContext())
        })
    }
}