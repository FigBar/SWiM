package com.example.bmi

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.logic.HistoryElement
import com.example.bmi.recycler_view.HistoryAdapter
import com.example.bmi.services.BmiHistoryServiceSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bmi_history.*
import java.util.ArrayList

class BmiHistoryActivity : AppCompatActivity() {

    private lateinit var prefsService: BmiHistoryServiceSharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dataSet: List<HistoryElement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)

        prefsService = BmiHistoryServiceSharedPreferences(
            this.getSharedPreferences(
                BmiHistoryServiceSharedPreferences.PREFS_FILENAME,
                Context.MODE_PRIVATE)
        )
        dataSet = prefsService.readAllRecords()
        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(dataSet)

        recyclerView = history_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}
