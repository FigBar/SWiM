package com.example.pictureuploader.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.pictureuploader.R
import com.squareup.picasso.Picasso


class PhotoFragment : Fragment() {

    companion object {
        const val URL = "URL"

        fun newInstance(url: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(URL, url)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val photoView = inflater.inflate(R.layout.fragment_photo, container, false)
        val photoDisplay = photoView.findViewById<ImageView>(R.id.photo_view)
        Picasso.get()
            .load(arguments?.getString(URL))
            .placeholder(R.drawable.loading)
            .error(R.drawable.error_image)
            .into(photoDisplay)
        return photoView
    }


}
