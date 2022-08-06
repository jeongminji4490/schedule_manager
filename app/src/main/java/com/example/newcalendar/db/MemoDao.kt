package com.example.newcalendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newcalendar.model.entity.Memo

@Dao
interface MemoDao { // 메모 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMemo(item: Memo)

    @Query("DELETE FROM memo WHERE serialNum = :serialNum")
    fun deleteMemo(serialNum: Int)

    @Query("SELECT * FROM memo")
    fun getAllMemo() : LiveData<List<Memo>>

    @Query("UPDATE memo SET completion = :completion where serialNum = :serialNum")
    fun changeCompletion(completion: Boolean, serialNum: Int)

    @Query("UPDATE memo SET content = :content where serialNum = :serialNum")
    fun changeContent(content: String, serialNum: Int)
}