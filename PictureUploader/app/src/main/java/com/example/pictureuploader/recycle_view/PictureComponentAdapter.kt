package com.example.pictureuploader.recycle_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureuploader.R
import com.example.pictureuploader.logic.PictureRecord
import com.example.pictureuploader.services.FirebaseTagService
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.text.SimpleDateFormat
import java.util.*


class PictureComponentAdapter(private val dataSet: MutableList<PictureRecord>, private val parentContext: Context) :
    RecyclerView.Adapter<PictureComponentAdapter.PictureComponentViewHolder>() {

    private val tagService: FirebaseTagService = FirebaseTagService
    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureComponentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.picture_card_layout, parent, false)
        return PictureComponentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureComponentViewHolder, position: Int) {
        holder.titleDisplay.text = dataSet[position].title
        holder.dateDisplay.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dataSet[position].date)
        loadPictureAndTags(dataSet[position].url, holder)

    }

    private fun loadPictureAndTags(url: String, holder: PictureComponentViewHolder) {
        Picasso.with(parentContext)
            .load(url)
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    holder.pictureDisplay.setImageResource(R.drawable.loading)
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    holder.pictureDisplay.setImageResource(R.drawable.error_image)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    holder.pictureDisplay.setImageBitmap(bitmap)
                    FirebaseVision.getInstance().onDeviceImageLabeler
                        .processImage(FirebaseVisionImage.fromBitmap(bitmap!!))
                        .addOnSuccessListener {
                            holder.tagsDisplay.text = it.map { it.text }
                                .toTypedArray()
                                .take(3)
                                .joinToString(" #", prefix = "#")
                        }
                }
            }
            )
    }

    class PictureComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var pictureDisplay: ImageView
        var titleDisplay: TextView
        var dateDisplay: TextView
        var tagsDisplay: TextView

        init {
            pictureDisplay = itemView.findViewById(R.id.picture_display)
            titleDisplay = itemView.findViewById(R.id.title_display)
            dateDisplay = itemView.findViewById(R.id.date_display)
            tagsDisplay = itemView.findViewById(R.id.tags_display)
        }
    }


}