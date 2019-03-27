package com.example.bmi.services

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.bmi.R

class DisplayAttributeManager(private val context: Context) : DisplayAttributeService {

    override fun setTextColor(compToModify: TextView, matchParam: Double) {
        when {
            matchParam < 18.5 -> compToModify.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.lapisLazuli
                )
            )
            matchParam < 25 -> compToModify.setTextColor(ContextCompat.getColor(context, R.color.verdigris))
            matchParam < 30 -> compToModify.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.overweightOrange
                )
            )
            matchParam < 35 -> compToModify.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.obesityDarkOrange
                )
            )
            else -> compToModify.setTextColor(ContextCompat.getColor(context, R.color.pompeianRose))
        }
    }
}