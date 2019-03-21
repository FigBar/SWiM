package com.example.bmi

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.logic.HistoryElement
import com.example.bmi.recycler_view.HistoryAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bmi_history.*
import java.util.ArrayList

class BmiHistoryActivity : AppCompatActivity() {

    private var prefs: SharedPreferences? = null
    private val PREFS_FILENAME = "com.example.bmi.prefs"

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var dataSet: ArrayList<HistoryElement?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)

        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        readDataSet()
        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(dataSet)

        recyclerView = history_recycler_view.apply {
            //setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun readDataSet() {
        val jsonHistory = prefs!!.getString("HISTORY_ARRAY", "")
        val destType = object : TypeToken<ArrayList<HistoryElement?>>(){}.type
        dataSet = Gson().fromJson<ArrayList<HistoryElement?>>(jsonHistory, destType)
    }
}
