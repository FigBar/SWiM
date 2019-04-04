package com.example.pictureuploader.recycle_view

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureuploader.R
import com.example.pictureuploader.logic.PictureRecord
import com.example.pictureuploader.services.FirebaseTagService
import com.squareup.picasso.Picasso
import io.reactivex.Single
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
        dataSet[position].tags = tagArray
        holder.tagsDisplay.text = tagArray.take(3).joinToString(" #", prefix = "#")
    }

    private fun getBitmapSingle(picasso: Picasso, url: String): Single<Bitmap> = Single.create {
        try {
            if(!it.isDisposed) {
                val bitmap: Bitmap = picasso.load(url).get()
                it.onSuccess(bitmap)
            }
        } catch (e: Throwable) {
            it.onError(e)
        }
    }

    private fun loadPicture(url: String, container: ImageView) {
        Picasso.with(parentContext)
            .load(url)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error_image)
            .into(
                onBitmapLoaded = { bitmap: Bitmap, _: Picasso.LoadedFrom ->
                    {
                        container.setImageBitmap(bitmap)

                    }
                },
                onBitmapFailed = {},
                onPrepareLoad = {}
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