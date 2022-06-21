package com.example.newcalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.newcalendar.databinding.FragmentMemoBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.logger.KOIN_TAG

class MemoFragment : Fragment(R.layout.fragment_memo) {

    private val binding by viewBinding(FragmentMemoBinding::bind,
        onViewDestroyed = {
            // reset view
            it.todoListView.adapter = null
            Log.e(TAG, "onViewDestroyed()")
        })

    private var setJob : Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e(TAG, "onViewCreated()")

        val serialNum = 0
        val viewModel : ViewModel by inject()
        val adapter = MemoAdapter(requireContext(), viewModel)
        val itemTouchHelper = ItemTouchHelper(SwipeController(adapter))
        itemTouchHelper.attachToRecyclerView(binding.todoListView)

        val month =  (CalendarDay.today().month + 1).toString()
        val day = CalendarDay.today().day.toString()
        val date = "${month}월 ${day}일"
        Log.e("Memo", CalendarDay.today().date.toString())

        binding.todayDate.text = date

        binding.saveBtn.setOnClickListener {
            val memo = binding.memoEdit.text.toString()
            if (memo.isNotEmpty()){
                setJob = lifecycleScope.launch {
                    withContext(Dispatchers.IO){
                        viewModel.addMemo(MemoDataModel(serialNum, memo, false))
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

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        setJob?.cancel()
        Log.e(TAG, "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy()")
    }

    companion object{
        const val TAG = "MemoFragment"
    }
}