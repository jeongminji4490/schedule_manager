package com.example.newcalendar.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.ScheduleItemBinding
import com.example.newcalendar.model.entity.Schedule

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.Holder>() {

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

    override fun getItemCount(): Int = list.size

    fun removeAll(){
        list.clear()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){
        fun onBind(item : Schedule, size: Int){
            binding.schedule = item
        }
    }
}
