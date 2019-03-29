package com.example.bmi.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.R
import com.example.bmi.logic.HistoryElement


class HistoryAdapter (private val dataSet: List<HistoryElement>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun getItemCount(): Int = dataSet.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bmiTextView?.text = dataSet[position].bmiResult
        holder.massTextView?.text = dataSet[position].mass
        holder.heightTextView?.text = dataSet[position].height
        holder.dateTextView?.text = dataSet[position].date
        holder.indexDisplay?.text = (position + 1).toString()

    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bmiTextView: TextView? = null
        var massTextView: TextView? = null
        var heightTextView: TextView? = null
        var dateTextView: TextView? = null
        var indexDisplay: TextView? = null
        init {
            bmiTextView = itemView.findViewById(R.id.bmiTV)
            massTextView = itemView.findViewById(R.id.massTV)
            heightTextView = itemView.findViewById(R.id.heightTV)
            dateTextView = itemView.findViewById(R.id.dateTV)
            indexDisplay = itemView.findViewById(R.id.indexDisplay)

        }
    }

}