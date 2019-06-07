package com.example.musicplayer

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import com.example.musicplayer.model.MusicRecord
import com.example.musicplayer.repositories.MusicTracksRepository
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private val musicRepository = MusicTracksRepository
    private var currentTrack: MusicRecord? = null
    private var currentTrackNumber = 0
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var resumePosition: Int = 0
    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        currentTrackNumber = intent?.extras!!.getInt(MainActivity.TRACK_NUMBER)
        setPlayedTrack()
    }

    private fun setPlayedTrack() {
        currentTrack = musicRepository.musicRecordsList[currentTrackNumber]
        loadTrackInfo()
        setUpMediaPlayer()
        play_pause_button.setImageResource(R.drawable.pause)
    }

    private fun setUpMediaPlayer() {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(currentTrack?.path)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.prepareAsync()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        stopMusic()
        if (currentTrackNumber + 1 <= musicRepository.musicRecordsList.size - 1) {
            currentTrackNumber += 1
            setPlayedTrack()
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        player_seekBar.max = mediaPlayer.duration
        track_length_display.text = convertTime(mediaPlayer.duration)
        setSeekBarChangedListener()
        playMusic()
        modifySeekBar()
    }

    private fun setSeekBarChangedListener() {
        player_seekBar.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun convertTime(milis: Int): String {
        var sec = milis/1000
        val min = sec/60
        sec %= 60
        if(sec < 10) return "$min:0$sec"
        return "$min:$sec"
    }

    private fun modifySeekBar() {
        player_seekBar.progress = mediaPlayer.currentPosition
        current_time_display.text = convertTime(mediaPlayer.currentPosition)
        if(mediaPlayer.isPlaying){
            runnable = Runnable{
                runOnUiThread {
                    modifySeekBar()
                }
            }
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun playMusic() {
        if (!mediaPlayer.isPlaying) {
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
        if (mediaPlayer.isPlaying) {
            pauseMusic()
            play_pause_button.setImageResource(R.drawable.play)
        } else {
            resumeMusic()
            modifySeekBar()
            play_pause_button.setImageResource(R.drawable.pause)
        }

    }

    fun onNextClicked(view: View) {
        if (currentTrackNumber + 1 <= musicRepository.musicRecordsList.size - 1) {
            currentTrackNumber += 1
            setPlayedTrack()
        }
    }

    fun onBackClicked(view: View) {
        if (currentTrackNumber - 1 >= 0) {
            currentTrackNumber -= 1
            setPlayedTrack()
        }
    }

    private fun loadTrackInfo() {
        val dataRetriever = MediaMetadataRetriever()
        dataRetriever.setDataSource(currentTrack?.path)
        val cover = dataRetriever.embeddedPicture
        if (cover != null) {
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
