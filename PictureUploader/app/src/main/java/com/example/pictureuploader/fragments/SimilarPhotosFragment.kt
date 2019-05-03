package com.example.pictureuploader.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.example.pictureuploader.R
import com.example.pictureuploader.model.PictureRecord

import com.example.pictureuploader.recycle_view.SimilarPicturesAdapter
import kotlinx.android.synthetic.main.activity_main.*


class SimilarPhotosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        private const val PICTURE_RECORD_LIST = "picture_record_list"
        private const val POSITION = "position"

        fun newInstance(dataSet: ArrayList<PictureRecord>, position: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PICTURE_RECORD_LIST, dataSet)
                    putInt(POSITION, position)
                }
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewManager = GridLayoutManager(activity, 3)
        val dataSet = findSimilarPhotos()
        viewAdapter = SimilarPicturesAdapter(dataSet)
        recyclerView = main_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_similar_photos, container, false)
    }

    private fun findSimilarPhotos(): MutableList<PictureRecord> {
        val dataSet = arguments!!.getParcelableArrayList<PictureRecord>(PICTURE_RECORD_LIST)
        val index = arguments!!.getInt(POSITION)
        val chosenPicture = dataSet.removeAt(index)
        dataSet!!.sortWith(compareBy { it.assessSimilarity(chosenPicture) })
        return dataSet.takeLast(6).filter { it.assessSimilarity(chosenPicture) != 0 }.toMutableList()

    }


}
