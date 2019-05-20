package com.example.pictureuploader.recycle_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureuploader.R
import com.example.pictureuploader.model.PictureRecord
import com.squareup.picasso.Picasso

class SimilarPicturesAdapter(private val dataSet: MutableList<PictureRecord>) : RecyclerView.Adapter<SimilarPicturesAdapter.SimilarPicturesViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarPicturesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.similar_photo_card_layout, parent, false)
        return SimilarPicturesViewHolder(itemView)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SimilarPicturesViewHolder, position: Int) {
        Picasso.get()
            .load(dataSet[position].url)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error_image)
            .into(holder.pictureDisplay)
    }

    class SimilarPicturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var pictureDisplay: ImageView

        init {
            pictureDisplay = itemView.findViewById(R.id.recycler_photo_display)
        }
    }


}