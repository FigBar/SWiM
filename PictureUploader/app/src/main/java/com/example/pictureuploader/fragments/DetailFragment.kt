package com.example.pictureuploader.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.pictureuploader.R
import com.example.pictureuploader.model.PictureRecord
import java.text.SimpleDateFormat
import java.util.*


class DetailFragment : Fragment() {

    companion object {
        private const val PICTURE_RECORD = "picture_record"

        fun newInstance(pictureRecord: PictureRecord) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PICTURE_RECORD, pictureRecord)
                    }
                }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val detailView =  inflater.inflate(R.layout.fragment_detail, container, false)
        val titleView = detailView.findViewById<TextView>(R.id.title_display)
        val tagView = detailView.findViewById<TextView>(R.id.tags_display)
        val dateView = detailView.findViewById<TextView>(R.id.date_display)

        val pictureRecord = arguments?.getParcelable<PictureRecord>(PICTURE_RECORD)

        titleView.text = pictureRecord?.title
        tagView.text = pictureRecord!!.tags.joinToString(" #", prefix = "#")
        dateView.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(pictureRecord.date.time)
        return detailView
    }


}
