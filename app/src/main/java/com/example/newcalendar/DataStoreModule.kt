package com.example.newcalendar

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DateSaveModule (private val context: Context) {
    private val Context.datastore by preferencesDataStore(name = "datastore")
    private val dateKey = stringPreferencesKey("DATE_KEY")

    val date : Flow<String> = context.datastore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {
            it[dateKey] ?: ""
        }

    suspend fun setDate(date : String){
        context.datastore.edit {
            it[dateKey] = date
        }
    }

}