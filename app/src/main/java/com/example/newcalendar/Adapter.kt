package com.example.newcalendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.MemoItemBinding
import com.example.newcalendar.databinding.ResultItemBinding
import com.example.newcalendar.databinding.ScheduleItemBinding
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import java.util.Locale.filter

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

class TestAdapter(var input: List<ScheduleDataModel>) : RecyclerView.Adapter<TestAdapter.Holder>(), Filterable {

    //var list : List<ScheduleDataModel> = ArrayList()
    private lateinit var binding : ResultItemBinding

    var filteredList = ArrayList<ScheduleDataModel>()
    var itemFilter = ItemFilter()
    init{
        input.let {
            filteredList.addAll(input)
        }
    }

    interface ItemClick{
        fun onClick(view: View, position: Int, list: List<ScheduleDataModel>)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ResultItemBinding.inflate(inflater, parent, false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(filteredList[position], itemCount)
        if (itemClick!=null){
            holder.view.setOnClickListener{ v ->
                itemClick?.onClick(v, position, filteredList)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){

        fun onBind(item : ScheduleDataModel, size: Int){
            binding.item = item
        }
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            Log.e("TestAdapter", "charSequence : $charSequence")

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filtered: ArrayList<ScheduleDataModel> = ArrayList()

            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            when {
                filterString.trim { it <= ' ' }.isEmpty() -> {
                    results.values = input
                    results.count = input.size
                    return results
                    //공백제외 2글자 이하인 경우 -> 이름으로만 검색
                }
                filterString.trim { it <= ' ' }.length <= 2 -> {
                    for (item in input) {
                        if (item.content.contains(filterString)) {
                            filtered.add(item)
                            Log.e("TestAdapter", filtered.toString()) // ok
                        }
                    }
                    //그 외의 경우(공백제외 2글자 초과) -> 이름/전화번호로 검색
                }
                else -> {
                    for (item in input) {
                        if (item.date.contains(filterString)) {
                            filtered.add(item)
                            //Log.e("TestAdapter", filteredList.toString()) // ok
                        }
                    }
                }
            }
            results.values = filtered
            results.count = filtered.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filteredResults : FilterResults) {
            filteredList.clear()
            filteredList.addAll(filteredResults.values as ArrayList<ScheduleDataModel>)
            Log.e("TestAdapter : filteredResults.value", filteredResults.values.toString()) // ok
            Log.e("TestAdapter : filteredList", filteredList.toString()) // ok
            notifyDataSetChanged()
        }
    }
}

object Adapter {
    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("item")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, item: List<ScheduleDataModel>?){
        if (recyclerView.adapter == null){
            val adapter = item?.let { TestAdapter(it) }
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
        }
        if (item != null) {
            (recyclerView.adapter as TestAdapter).filteredList = item as ArrayList<ScheduleDataModel>
        }
        recyclerView.adapter?.notifyDataSetChanged()
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

