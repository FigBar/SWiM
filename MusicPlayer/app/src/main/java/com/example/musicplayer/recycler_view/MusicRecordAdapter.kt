package com.example.musicplayer.recycler_view


import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.repositories.MusicTracksRepository
import com.example.musicplayer.services.MediaPlayerService
import kotlinx.android.parcel.Parcelize


class MusicRecordAdapter(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.Adapter<MusicRecordAdapter.PictureRecordViewHolder>() {

    companion object {
        const val TRACK_NUMBER = "track_number"
    }

    override fun getItemCount(): Int = MusicTracksRepository.musicRecordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureRecordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_card_layout, parent, false)
        return PictureRecordViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: PictureRecordViewHolder, position: Int) {
        loadAlbumCover(MusicTracksRepository.musicRecordsList[position].path, holder.songArtDisplay)
        holder.titleDisplay.text = MusicTracksRepository.musicRecordsList[position].title
        holder.artistDisplay.text = MusicTracksRepository.musicRecordsList[position].artist
        preparePlayBtn(holder, position)
        holder.itemView.setOnClickListener {
            if (MediaPlayerService.currentlyPlaying == position)
                startPlayerActivity(holder, position)
            else
                MediaPlayerService.changeTrack(position, this)
        }
    }

    private fun preparePlayBtn(holder: PictureRecordViewHolder, position: Int) {
        if (MediaPlayerService.currentlyPlaying == position) {
            holder.playButton.visibility = View.VISIBLE
            if (MediaPlayerService.mediaPlayer.isPlaying)
                holder.playButton.setImageResource(R.drawable.pause_24)
            else
                holder.playButton.setImageResource(R.drawable.play_24)
            holder.playButton.setOnClickListener { onPlayPauseClicked(holder) }
        } else {
            holder.playButton.visibility = View.INVISIBLE
        }
    }

    private fun onPlayPauseClicked(holder: PictureRecordViewHolder) {
        if (MediaPlayerService.mediaPlayer.isPlaying) {
            MediaPlayerService.isPlaying = false
            MediaPlayerService.pauseMusic()
            holder.playButton.setImageResource(R.drawable.play_24)
        } else {
            MediaPlayerService.isPlaying = true
            MediaPlayerService.playMusic()
            holder.playButton.setImageResource(R.drawable.pause_24)
        }
    }

    private fun startPlayerActivity(holder: PictureRecordViewHolder, position: Int) {
        val playerIntent = Intent(holder.itemView.context, PlayerActivity::class.java)
        val fragBundle = Bundle()
        fragBundle.putInt(TRACK_NUMBER, position)
        playerIntent.putExtras(fragBundle)
        startActivity(holder.itemView.context, playerIntent, null)
    }



    private fun loadAlbumCover(path: String, imageDisplay: ImageView) {
        val dataRetriever = MediaMetadataRetriever()
        dataRetriever.setDataSource(path)
        val cover = dataRetriever.embeddedPicture
        if (cover != null) {
            val coverImage = BitmapFactory.decodeByteArray(cover, 0, cover.size)
            imageDisplay.setImageBitmap(coverImage)
        } else {
            imageDisplay.setImageResource(R.drawable.cover_failed)
        }
    }

    fun updateCurrentTrack(){
        notifyDataSetChanged()
        layoutManager.scrollToPosition(MediaPlayerService.currentlyPlaying)
    }

    class PictureRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var songArtDisplay: ImageView
        var titleDisplay: TextView
        var artistDisplay: TextView
        var playButton: ImageButton

        init {
            songArtDisplay = itemView.findViewById(R.id.song_art_display)
            titleDisplay = itemView.findViewById(R.id.title_display)
            artistDisplay = itemView.findViewById(R.id.artist_display)
            playButton = itemView.findViewById(R.id.adapter_play_btn)
        }

    }


}