package com.example.pictureuploader.recycle_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureuploader.R
import com.example.pictureuploader.logic.PictureRecord
import com.example.pictureuploader.services.FirebaseTagService
import com.squareup.picasso.Picasso
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
        loadPicture(dataSet[position].url, holder.pictureDisplay)
        val tagArray = tagService.generateTags(holder.pictureDisplay.drawable.toBitmap())
        dataSet[position].tags = tagArray
        holder.tagsDisplay.text = tagArray.take(3).joinToString(" #", prefix = "#")
    }

    private fun loadPicture(url: String, container: ImageView) {
        Picasso.with(parentContext)
            .load(url)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error_image)
            .into(container)
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