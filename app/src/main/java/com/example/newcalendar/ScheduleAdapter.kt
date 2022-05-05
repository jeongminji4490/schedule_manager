package com.example.newcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.ScheduleItemBinding

class ScheduleAdapter(private val context: Context) : RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    lateinit var list : ArrayList<Schedule>
    lateinit var binding : ScheduleItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ScheduleItemBinding.inflate(LayoutInflater.from(context))
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position])
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
        fun onBind(item : Schedule){
            binding.schedule = item
        }
    }
}