package com.example.newcalendar.view.ui.memo

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newcalendar.ButtonState

class SwipeController() : ItemTouchHelper.Callback() {

    private var swipeBack = false
    private val buttonWidth = 200f //버튼 너비 지정
    private var buttonsShowedState = ButtonState.GONE
    private var buttonInstance: RectF? = null //버튼 객체 초기 지정
    private lateinit var listener: ItemTouchHelperListener
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    constructor(listener: ItemTouchHelperListener) : this() {
        this.listener = listener
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val draw_flags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipe_flags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(draw_flags, swipe_flags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return listener.onItemMove(viewHolder.adapterPosition,target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.adapterPosition);
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var dX = dX
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (buttonsShowedState !== ButtonState.GONE) {
                if (buttonsShowedState === ButtonState.LEFT_VISIBLE) //오른쪽으로 스와이프 했을 때
                    dX = dX.coerceAtLeast(buttonWidth)
                if (buttonsShowedState === ButtonState.RIGHT_VISIBLE) //왼쪽으로 스와이프 했을 때
                    dX = dX.coerceAtMost(-buttonWidth)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            if (buttonsShowedState === ButtonState.GONE) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        currentItemViewHolder = viewHolder
        drawButtons(c, currentItemViewHolder!!)
    }

    private fun drawButtons(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder
    ) { //레이아웃이 아닌 클래스에서 직접 버튼 구현
        val buttonWidthWithOutPadding = buttonWidth - 10
        val corners = 5f
        val itemView: View = viewHolder.itemView
        val p = Paint()
        buttonInstance = null

        if (buttonsShowedState === ButtonState.LEFT_VISIBLE) {
            val leftButton = RectF(
                (itemView.left + 10).toFloat(),
                (itemView.top + 10).toFloat(),
                itemView.left + buttonWidthWithOutPadding,
                (itemView.bottom - 10).toFloat()
            )
            p.color = Color.parseColor("#34568B")
            c.drawRoundRect(leftButton, corners, corners, p)
            drawText("수정", c, leftButton, p)
            buttonInstance = leftButton
        } else if (buttonsShowedState === ButtonState.RIGHT_VISIBLE) {
            val rightButton = RectF(
                itemView.right - buttonWidthWithOutPadding, (itemView.top + 10).toFloat(),
                (itemView.right - 10).toFloat(), (itemView.bottom - 10).toFloat()
            )
            p.color = Color.parseColor("#34568B")
            c.drawRoundRect(rightButton, corners, corners, p)
            drawText("삭제", c, rightButton, p)
            buttonInstance = rightButton
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint) { //버튼 내에 글씨 삽입
        val textSize = 45f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize
        val textWidth = p.measureText(text)
        c.drawText(
            text,
            button.centerX() - textWidth / 2,
            button.centerY() + textSize / 2,
            p
        )
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { view, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonWidth) buttonsShowedState =
                    ButtonState.RIGHT_VISIBLE else if (dX > buttonWidth) buttonsShowedState =
                    ButtonState.LEFT_VISIBLE
                if (buttonsShowedState !== ButtonState.GONE) {
                    setTouchDownListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setItemClickable(recyclerView, false)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            super@SwipeController.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                0f,
                dY,
                actionState,
                isCurrentlyActive
            )
            recyclerView.setOnTouchListener { v, event -> false }
            setItemClickable(recyclerView, true)
            swipeBack = false
            /*인터페이스??*/if (buttonInstance != null && buttonInstance!!.contains(
                event.x,
                event.y
            )
        ) {
            if (buttonsShowedState === ButtonState.LEFT_VISIBLE) {
                listener.onLeftClick(viewHolder.adapterPosition, viewHolder)
            } else if (buttonsShowedState === ButtonState.RIGHT_VISIBLE) {
                listener.onRightClick(viewHolder.adapterPosition, viewHolder)
            }
        }
            buttonsShowedState = ButtonState.GONE
            currentItemViewHolder = null
            false
        }
    }

    private fun setItemClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }
}