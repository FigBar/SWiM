package com.example.bmi.services

import com.example.bmi.logic.HistoryElement

interface BmiHistoryService {
    fun saveHistoryRecord(elem: HistoryElement)
    fun readAllRecords() : List<HistoryElement>
}