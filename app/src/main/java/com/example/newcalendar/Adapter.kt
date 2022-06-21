package com.example.newcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.MemoItemBinding
import com.example.newcalendar.databinding.ScheduleItemBinding
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*

class ScheduleAdapter(
    private val context: Context,
    private val viewModel: ViewModel
    ) : RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    var list = ArrayList<Schedule>()
    private lateinit var binding : ScheduleItemBinding

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<Schedule>)
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

    fun addItems(item : Schedule){
        list.add(item)
    }

    fun removeAll(){
        list.clear()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){

        private var importanceImg = view.findViewById<ImageView>(R.id.importance_img) // 중요도 이미지
        private val redImg = ContextCompat.getDrawable(context, R.drawable.red_most_important)
        private val blueImg = ContextCompat.getDrawable(context, R.drawable.blue_moderately_important)
        private val yellowImg = ContextCompat.getDrawable(context, R.drawable.yellow_least_important)

        fun onBind(item : Schedule, size: Int){
            binding.schedule = item
            when(item.importance){ // 중요도 이미지
                0 -> { importanceImg.setImageDrawable(redImg) }
                1 -> { importanceImg.setImageDrawable(blueImg) }
                2 -> { importanceImg.setImageDrawable(yellowImg) }
            }
        }
    }
}

class MemoAdapter (
    private val context: Context,
    private val viewModel: ViewModel
    ) : RecyclerView.Adapter<MemoAdapter.Holder>(), ItemTouchHelperListener {

    private val list = ArrayList<MemoDataModel>()
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
            binding.completionBox.setOnCheckedChangeListener { button, b ->
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

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        return false
    }

    override fun onItemSwipe(position: Int) {
        //
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        val dialog = MemoModifyFragment(list[position].content, list[position].serialNum)
        dialog.show((context as FragmentActivity).supportFragmentManager, "MemoModifyFragment")
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) { // 삭제
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteMemo(list[position].serialNum)
        }
    }
}

//object Adapter{
//
//    @BindingAdapter("item")
//    @JvmStatic
//    fun setItems(view: RecyclerView, item: ArrayList<Schedule>){
//        if (view.adapter==null){
//            view.adapter = ScheduleAdapter()
//        }
//
//        val adapter = view.adapter as ScheduleAdapter
//
//        adapter.list = item
//    }
//}