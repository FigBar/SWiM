package com.example.pictureuploader.recycle_view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureuploader.FragmentActivity
import com.example.pictureuploader.R
import com.example.pictureuploader.model.PictureRecord
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PictureComponentAdapter(private val dataSet: MutableList<PictureRecord>, private val parentContext: Context) :
    RecyclerView.Adapter<PictureComponentAdapter.PictureComponentViewHolder>() {

    companion object {
        private var clickListener: ClickListener? = null
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        PictureComponentAdapter.clickListener = clickListener
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureComponentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.picture_card_layout, parent, false)
        return PictureComponentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureComponentViewHolder, position: Int) {
        holder.titleDisplay.text = dataSet[position].title
        holder.dateDisplay.text =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dataSet[position].date.time)
        loadPictureAndTags(dataSet[position].url, holder, position)

    }


    private fun loadPictureAndTags(url: String, holder: PictureComponentViewHolder, position: Int) {

        Picasso.get()
            .load(url)
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    holder.pictureDisplay.setImageResource(R.drawable.loading)

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    holder.pictureDisplay.setImageResource(R.drawable.error_image)

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    holder.pictureDisplay.setImageBitmap(bitmap)
                    FirebaseVision.getInstance().onDeviceImageLabeler
                        .processImage(FirebaseVisionImage.fromBitmap(bitmap!!))
                        .addOnSuccessListener {
                            val tagList = it.map { it.text }.toTypedArray()
                            holder.tagsDisplay.text = tagList.take(3).joinToString(" #", prefix = "#")
                            dataSet[position].tags.clear()
                            dataSet[position].tags.addAll(tagList)
                        }
                }
            })
    }

    class PictureComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var pictureDisplay: ImageView
        var titleDisplay: TextView
        var dateDisplay: TextView
        var tagsDisplay: TextView

        init {
            pictureDisplay = itemView.findViewById(R.id.picture_display)
            titleDisplay = itemView.findViewById(R.id.title_display)
            dateDisplay = itemView.findViewById(R.id.date_display)
            tagsDisplay = itemView.findViewById(R.id.tags_display)
            pictureDisplay.clipToOutline = true
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener?.onItemClick(adapterPosition, v)
        }
    }

    interface ClickListener {
        fun onItemClick(position: Int, view: View)
    }

}