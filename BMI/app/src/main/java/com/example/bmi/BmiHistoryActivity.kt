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

    companion object {
        const val PREFS_FILENAME = "com.example.bmi.prefs"
        private const val KEY = "history_list"
    }
    private var prefs: SharedPreferences? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dataSet: List<HistoryElement?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)

        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        readDataSet()
        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(dataSet)

        recyclerView = history_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun readDataSet() {
        val jsonHistory = prefs!!.getString(KEY, "[]")
        class Token : TypeToken<List<HistoryElement?>>()
        dataSet = Gson().fromJson<ArrayList<HistoryElement?>>(jsonHistory, Token().type)
    }
}
