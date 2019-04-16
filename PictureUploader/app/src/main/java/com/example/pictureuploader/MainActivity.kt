package com.example.pictureuploader

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureuploader.logic.PictureRecord
import com.example.pictureuploader.recycle_view.PictureComponentAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val NEW_PICTURE_REQUEST = 1
    }

    private var pictureRecordsList: MutableList<PictureRecord> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewManager = LinearLayoutManager(this)
        viewAdapter = PictureComponentAdapter(pictureRecordsList, this)
        recyclerView = main_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        setRecyclerViewItemTouchListener()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.add_picture_card -> onAddSelected()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onAddSelected(): Boolean {
        val intent = Intent(this, AddPictureActivity::class.java)
        startActivityForResult(intent, NEW_PICTURE_REQUEST)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == NEW_PICTURE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val newPictureRecord : PictureRecord? = data?.getParcelableExtra(AddPictureActivity.REPLY_KEY)
                addNewPictureRecord(newPictureRecord!!)
            }
        }
    }



    private fun addNewPictureRecord(newRecord: PictureRecord) {
        pictureRecordsList.add(0, newRecord)
        viewAdapter.notifyItemInserted(0)
        viewManager.scrollToPosition(0)
    }


    private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                pictureRecordsList.removeAt(position)
                recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
