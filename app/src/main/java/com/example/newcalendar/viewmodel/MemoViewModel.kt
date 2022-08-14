package com.example.newcalendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendar.db.MemoDao
import com.example.newcalendar.model.entity.Memo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoViewModel(
    private val mDao : MemoDao
) : ViewModel() {

    fun getAllMemo() : LiveData<List<Memo>> = mDao.getAllMemo()

    fun addMemo(item : Memo){
        mDao.addMemo(item)
    }

    fun deleteMemo(serialNum: Int){
        mDao.deleteMemo(serialNum)
    }

    fun changeCompletion(completion: Boolean, serialNum: Int)
    = viewModelScope.launch {
        withContext(Dispatchers.IO){
            mDao.changeCompletion(completion, serialNum)
        }
    }

    fun changeContent(content: String, serialNum: Int) {
        mDao.changeContent(content, serialNum)
    }

}