package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.logic.HistoryElement
import com.example.bmi.recycler_view.HistoryAdapter
import kotlinx.android.synthetic.main.activity_bmi_history.*
import java.util.ArrayList

class BmiHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var dataSet: ArrayList<HistoryElement?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)

        dataSet = intent.getParcelableArrayListExtra("HISTORY_ARRAY")

        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(dataSet)

        recyclerView = history_recycler_view.apply {
            //setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
