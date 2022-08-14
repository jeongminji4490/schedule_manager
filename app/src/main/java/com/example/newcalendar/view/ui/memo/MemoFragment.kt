package com.example.newcalendar.view.ui.memo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.viewmodel.*
import com.example.newcalendar.R
import com.example.newcalendar.databinding.FragmentMemoBinding
import com.example.newcalendar.model.entity.Memo
import com.example.newcalendar.view.adapter.MemoAdapter
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoFragment : Fragment(R.layout.fragment_memo) {

    private val binding by viewBinding(FragmentMemoBinding::bind,
        onViewDestroyed = { binding ->
            binding.todoListView.adapter = null
        })

    private val memoViewModel : MemoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serialNum = 0 // 메모 일련번호
        val adapter = MemoAdapter(requireContext(), memoViewModel)

        // RecyclerView 스와이프 기능
        val itemTouchHelper = ItemTouchHelper(SwipeController(adapter))
        itemTouchHelper.attachToRecyclerView(binding.todoListView)

        // 상단에 날짜 표시
        val date = run {
            val month =  (CalendarDay.today().month + 1).toString()
            val day = CalendarDay.today().day.toString()
            "${month}월 ${day}일"
        }
        binding.todayDate.text = date
        binding.todayDate.text

        // 메모 저장
        binding.saveBtn.setOnClickListener {
            val memo = binding.memoEdit.text.toString()
            if (memo.isNotEmpty()){
                lifecycleScope.launch {
                    withContext(Dispatchers.IO){
                        memoViewModel.addMemo(Memo(serialNum, memo, false))
                    }
                }
            } else {
                Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // 모든 메모 가져오기
        memoViewModel.getAllMemo().observe(viewLifecycleOwner, Observer { list ->
            adapter.removeAll()
            list?.let {
                adapter.list = it as ArrayList<Memo>
            }
            binding.todoListView.adapter = adapter
            binding.todoListView.layoutManager = LinearLayoutManager(requireContext())
        })
    }
}