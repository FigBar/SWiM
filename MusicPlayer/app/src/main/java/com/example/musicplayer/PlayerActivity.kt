package com.example.musicplayer

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.musicplayer.model.MusicRecord
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private var currentTrack: MusicRecord? = null
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var resumePosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        currentTrack = intent?.extras!!.getParcelable(MainActivity.TRACK)
        loadTrackInfo()
        setUpMediaPlayer()
        play_pause_button.setImageResource(R.drawable.pause)
    }

    private fun setUpMediaPlayer(){
        mediaPlayer.reset()
        mediaPlayer.setDataSource(currentTrack?.path)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.prepareAsync()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        stopMusic()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        playMusic()
    }
    private fun playMusic() {
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            resumePosition = mediaPlayer.currentPosition
        }
    }

    private fun resumeMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(resumePosition)
            mediaPlayer.start()
        }
    }

    fun onPlayPauseClicked(view: View) {
        if(mediaPlayer.isPlaying){
            pauseMusic()
            play_pause_button.setImageResource(R.drawable.play)
        } else {
            resumeMusic()
            play_pause_button.setImageResource(R.drawable.pause)
        }

    }

    private fun loadTrackInfo(){
        val dataRetriever = MediaMetadataRetriever()
        dataRetriever.setDataSource(currentTrack?.path)
        val cover = dataRetriever.embeddedPicture
        if(cover != null) {
            val coverImage = BitmapFactory.decodeByteArray(cover, 0, cover.size)
            player_cover_display.setImageBitmap(coverImage)
        } else {
            player_cover_display.setImageResource(R.drawable.cover_failed)
        }
        player_title_display.text = currentTrack?.title
        player_artist_display.text = currentTrack?.artist
    }

    override fun onResume() {
        super.onResume()
        setUpMediaPlayer()
    }
    override fun onPause() {
        super.onPause()
        stopMusic()
    }

}
