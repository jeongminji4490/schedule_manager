package com.example.newcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.databinding.ScheduleItemBinding

class ScheduleAdapter(private val context: Context) : RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    private var list = ArrayList<Schedule>()
    lateinit var binding : ScheduleItemBinding

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<Schedule>)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ScheduleItemBinding.inflate(LayoutInflater.from(context))
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position])
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
        private var importanceImg = view.findViewById<ImageView>(R.id.importance_img)
        private val redImg = ContextCompat.getDrawable(context, R.drawable.red_most_important)
        private val blueImg = ContextCompat.getDrawable(context, R.drawable.blue_moderately_important)
        private val yellowImg = ContextCompat.getDrawable(context, R.drawable.yellow_least_important)
        fun onBind(item : Schedule){
            binding.schedule = item
            when(item.importance){
                0 -> { importanceImg.setImageDrawable(redImg) }
                1 -> { importanceImg.setImageDrawable(blueImg) }
                2 -> { importanceImg.setImageDrawable(yellowImg) }
            }
        }
    }
}