package com.example.musicplayer

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import com.example.musicplayer.model.MusicRecord
import com.example.musicplayer.recycler_view.MusicRecordAdapter
import com.example.musicplayer.repositories.MusicTracksRepository
import com.example.musicplayer.services.MediaPlayerService
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {

    private val musicRepository = MusicTracksRepository
    var currentTrackNumber = -1
    private lateinit var currentTrack: MusicRecord
    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        currentTrackNumber = intent?.extras!!.getInt(MusicRecordAdapter.TRACK_NUMBER)
        loadLayoutElements()
        modifySeekBar()
        updatePlayPauseButtonIcon()
        MediaPlayerService.mediaPlayer.setOnCompletionListener {
            MediaPlayerService.onCompletion(currentTrackNumber, this)
        }
    }

    fun prepareNewSong() {
        loadLayoutElements()
        modifySeekBar()
        updatePlayPauseButtonIcon()
    }

    private fun convertTime(milis: Int): String {
        var sec = milis/1000
        val min = sec/60
        sec %= 60
        if(sec < 10) return "$min:0$sec"
        return "$min:$sec"
    }




    private fun updatePlayPauseButtonIcon(){
        if (MediaPlayerService.mediaPlayer.isPlaying) {
            play_pause_button.setImageResource(R.drawable.pause)
        } else {
            play_pause_button.setImageResource(R.drawable.play)
        }
    }

    fun onPlayPauseClicked(view: View) {
        if (MediaPlayerService.mediaPlayer.isPlaying) {
            MediaPlayerService.isPlaying = false
            MediaPlayerService.pauseMusic()
        } else {
            MediaPlayerService.isPlaying = true
            MediaPlayerService.resumeMusic(player_seekBar)
            modifySeekBar()
        }
        updatePlayPauseButtonIcon()
    }

    fun onNextClicked(view: View) {
        currentTrackNumber = (currentTrackNumber + 1) % musicRepository.musicRecordsList.size
        MediaPlayerService.changeTrack(currentTrackNumber, this)
        loadLayoutElements()
        modifySeekBar()
        play_pause_button.setImageResource(R.drawable.pause)
    }

    fun onBackClicked(view: View) {
        currentTrackNumber -= 1
        if(currentTrackNumber < 0) currentTrackNumber = musicRepository.musicRecordsList.size - 1
        MediaPlayerService.changeTrack(currentTrackNumber, this)
        loadLayoutElements()
        modifySeekBar()
        play_pause_button.setImageResource(R.drawable.pause)
    }

    private fun loadLayoutElements(){
        currentTrack = MusicTracksRepository.musicRecordsList[currentTrackNumber]
        loadTrackInfo()
        track_length_display.text = convertTime(currentTrack.duration)
        player_seekBar.max = currentTrack.duration
        setSeekBarChangedListener()
    }

    private fun loadTrackInfo() {
        val dataRetriever = MediaMetadataRetriever()
        dataRetriever.setDataSource(currentTrack.path)
        val cover = dataRetriever.embeddedPicture
        if (cover != null) {
            val coverImage = BitmapFactory.decodeByteArray(cover, 0, cover.size)
            player_cover_display.setImageBitmap(coverImage)
        } else {
            player_cover_display.setImageResource(R.drawable.cover_failed)
        }
        player_title_display.text = currentTrack.title
        player_artist_display.text = currentTrack.artist
    }

    private fun setSeekBarChangedListener() {
        player_seekBar.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    MediaPlayerService.mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun modifySeekBar() {
        player_seekBar.progress = MediaPlayerService.mediaPlayer.currentPosition
        current_time_display.text = convertTime(MediaPlayerService.mediaPlayer.currentPosition)
        if(MediaPlayerService.mediaPlayer.isPlaying){
            runnable = Runnable{
                runOnUiThread {
                    modifySeekBar()
                }
            }
            handler.postDelayed(runnable, 1000)
        }
    }

}
