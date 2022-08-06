package com.example.newcalendar.view.ui.memo

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onItemMove(from_position: Int, to_position: Int): Boolean
    fun onItemSwipe(position: Int)
    fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?)
    fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?)
}