package com.example.bmi.services

import android.content.Context
import android.widget.TextView

interface DisplayAttributeService {
    fun setTextColor(compToModify: TextView, matchParam: Double)
}