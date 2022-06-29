package com.example.newcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.MemoItemBinding
import com.example.newcalendar.databinding.ScheduleItemBinding
import kotlinx.coroutines.*

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    var list = ArrayList<ScheduleDataModel>()
    private lateinit var binding : ScheduleItemBinding

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<ScheduleDataModel>)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ScheduleItemBinding.inflate(inflater, parent, false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position], itemCount)
        if (itemClick!=null){
            holder.view.setOnClickListener{ v ->
                itemClick?.onClick(v, position, list)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItems(item : ScheduleDataModel){
        list.add(item)
    }

    fun removeAll(){
        list.clear()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){

        fun onBind(item : ScheduleDataModel, size: Int){
            binding.schedule = item
        }
    }
}

class MemoAdapter (
    private val context: Context,
    private val viewModel: ViewModel
    ) : RecyclerView.Adapter<MemoAdapter.Holder>(), ItemTouchHelperListener {

    var list = ArrayList<MemoDataModel>()
    private lateinit var binding : MemoItemBinding

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<MemoDataModel>)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(context)
        binding = MemoItemBinding.inflate(inflater, parent, false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position])
        if (itemClick!=null){
            binding.completionBox.setOnClickListener { v ->
                itemClick?.onClick(v, position, list)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: MemoDataModel){
        list.add(item)
    }

    fun removeAll(){
        list.clear()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){

        fun onBind(item : MemoDataModel){
            binding.memo = item
            binding.completionBox.isChecked = item.completion
            binding.completionBox.setOnCheckedChangeListener { _, b ->
                if (b){
                    changeMemo(b, item.serialNum)
                }else{
                    changeMemo(b, item.serialNum)
                }
            }
        }

        private fun changeMemo(completion: Boolean, serialNum: Int){
            viewModel.changeCompletion(completion, serialNum)
        }
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        val dialog = MemoModifyFragment().apply {
            content = list[position].content
            serialNum = list[position].serialNum
        }
        dialog.show((context as FragmentActivity).supportFragmentManager, "MemoModifyFragment")
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) { // 삭제
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteMemo(list[position].serialNum)
        }
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        return false
    }

    override fun onItemSwipe(position: Int) {
        //
    }
}

object DataBindingUtil {
    @BindingAdapter("set_image")
    @JvmStatic
    fun setImageResource(view: ImageView, item: ScheduleDataModel){
        val redImg = ContextCompat.getDrawable(view.context, R.drawable.red_most_important)
        val blueImg = ContextCompat.getDrawable(view.context, R.drawable.blue_moderately_important)
        val yellowImg = ContextCompat.getDrawable(view.context, R.drawable.yellow_least_important)

        item.let { schedule ->
            when (schedule.importance){
                0 -> { view.setImageDrawable(redImg) }
                1 -> { view.setImageDrawable(blueImg) }
                2 -> { view.setImageDrawable(yellowImg) }
            }
        }
    }
}
