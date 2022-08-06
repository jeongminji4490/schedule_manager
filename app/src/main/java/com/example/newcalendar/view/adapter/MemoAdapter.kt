package com.example.newcalendar.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.view.ui.memo.ItemTouchHelperListener
import com.example.newcalendar.view.ui.memo.MemoModifyFragment
import com.example.newcalendar.R
import com.example.newcalendar.databinding.MemoItemBinding
import com.example.newcalendar.model.entity.Memo
import com.example.newcalendar.model.entity.Schedule
import com.example.newcalendar.viewmodel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoAdapter (
    private val context: Context,
    private val viewModel: MemoViewModel
) : RecyclerView.Adapter<MemoAdapter.Holder>(), ItemTouchHelperListener {

    var list = ArrayList<Memo>()
    private lateinit var binding: MemoItemBinding

    interface ItemClick {
        fun onClick(view: View, position: Int, list: ArrayList<Memo>)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(context)
        binding = MemoItemBinding.inflate(inflater, parent, false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position])
        if (itemClick != null) {
            binding.completionBox.setOnClickListener { v ->
                itemClick?.onClick(v, position, list)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeAll() {
        list.clear()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(item: Memo) {
            binding.memo = item
            binding.completionBox.isChecked = item.completion // 체크 유무 셋팅
            binding.completionBox.setOnCheckedChangeListener { _, b ->
                if (b) {
                    changeCompletion(b, item.serialNum)
                } else {
                    changeCompletion(b, item.serialNum)
                }
            }
        }
        private fun changeCompletion(completion: Boolean, serialNum: Int) { // 체크 유무 변경
            viewModel.changeCompletion(completion, serialNum)
        }
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) { // 메모 변경
        val dialog = MemoModifyFragment().apply {
            content = list[position].content
            serialNum = list[position].serialNum
        }
        dialog.show((context as FragmentActivity).supportFragmentManager, "MemoModifyFragment")
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) { // 메모 삭제
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteMemo(list[position].serialNum)
        }
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean = false

    override fun onItemSwipe(position: Int) { // }
    }

    object DataBindingUtil {
        @BindingAdapter("set_image")
        @JvmStatic
        fun setImageResource(view: ImageView, item: Schedule) { // 일정 중요도에 따라 이미지 셋팅
            val redImg =
                ContextCompat.getDrawable(view.context, R.drawable.red_most_important) // 1순위
            val blueImg =
                ContextCompat.getDrawable(view.context, R.drawable.blue_moderately_important) // 2순위
            val yellowImg =
                ContextCompat.getDrawable(view.context, R.drawable.yellow_least_important) // 3순위

            item.let { schedule ->
                when (schedule.importance) {
                    0 -> {
                        view.setImageDrawable(redImg)
                    }
                    1 -> {
                        view.setImageDrawable(blueImg)
                    }
                    2 -> {
                        view.setImageDrawable(yellowImg)
                    }
                }
            }
        }
    }
}