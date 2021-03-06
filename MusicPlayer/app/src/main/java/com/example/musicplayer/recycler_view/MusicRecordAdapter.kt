package com.example.musicplayer.recycler_view


import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.model.MusicRecord


class MusicRecordAdapter(private val dataSet: MutableList<MusicRecord>, private val clickListener: ClickListener) :
    RecyclerView.Adapter<MusicRecordAdapter.PictureRecordViewHolder>() {

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: PictureRecordViewHolder, position: Int) {
        loadAlbumCover(dataSet[position].path, holder.songArtDisplay)
        holder.titleDisplay.text = dataSet[position].title
        holder.artistDisplay.text = dataSet[position].artist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureRecordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_card_layout, parent, false)
        return PictureRecordViewHolder(itemView, clickListener)
    }

    private fun loadAlbumCover(path: String, imageDisplay: ImageView){
        val dataRetriever = MediaMetadataRetriever()
        dataRetriever.setDataSource(path)
        val cover = dataRetriever.embeddedPicture
        if(cover != null) {
            val coverImage = BitmapFactory.decodeByteArray(cover, 0, cover.size)
            imageDisplay.setImageBitmap(coverImage)
        } else {
            imageDisplay.setImageResource(R.drawable.cover_failed)
        }
    }

    class PictureRecordViewHolder(itemView: View, clickListener: ClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var songArtDisplay: ImageView
        var titleDisplay: TextView
        var artistDisplay: TextView
        private var clickListener: ClickListener

        init {
            songArtDisplay = itemView.findViewById(R.id.song_art_display)
            titleDisplay = itemView.findViewById(R.id.title_display)
            artistDisplay = itemView.findViewById(R.id.artist_display)
            this.clickListener = clickListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener.onItemClick(adapterPosition)
        }
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}