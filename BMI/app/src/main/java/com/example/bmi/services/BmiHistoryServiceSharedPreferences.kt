package com.example.bmi.services

import android.content.SharedPreferences
import com.example.bmi.logic.HistoryElement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BmiHistoryServiceSharedPreferences(private val prefs: SharedPreferences) : BmiHistoryService {

    private val gson = Gson()

    companion object {
        const val PREFS_FILENAME = "com.example.bmi.prefs"
        private const val KEY = "history_list"
    }

    override fun saveHistoryRecord(elem: HistoryElement) {
        val previousHistoryList = readAllRecords()
        val currentHistoryList = listOf(elem) + previousHistoryList.take(9)
        val jsonHistory = gson.toJson(currentHistoryList)
        with(prefs.edit()) {
            remove(KEY)
            putString(KEY, jsonHistory)
            apply()
        }
    }

    override fun readAllRecords() : List<HistoryElement> {
        val jsonHistory = prefs.getString(KEY, "[]")
        class Token : TypeToken<List<HistoryElement>>()
        return gson.fromJson<List<HistoryElement>>(jsonHistory, Token().type)
    }
}