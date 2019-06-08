package com.example.musicplayer.services

import android.media.MediaPlayer
import android.widget.SeekBar
import com.example.musicplayer.recycler_view.MusicRecordAdapter
import com.example.musicplayer.repositories.MusicTracksRepository


object MediaPlayerService {

    var mediaPlayer = MediaPlayer()
    var currentlyPlaying = -1
    var isPlaying = false

    private fun setUpMediaPlayer(trackIndex: Int) {
        currentlyPlaying = trackIndex
        isPlaying = true
        val track = MusicTracksRepository.musicRecordsList[currentlyPlaying]
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track.path)

    }

    private fun onCompletion(index: Int) {
        val nextTrackIndex = (index + 1) % MusicTracksRepository.musicRecordsList.size
        changeTrack(nextTrackIndex)
    }

    private fun onCompletion(index: Int, adapter: MusicRecordAdapter) {
        val nextTrackIndex = (index + 1) % MusicTracksRepository.musicRecordsList.size
        changeTrack(nextTrackIndex, adapter)
    }

    fun changeTrack(index: Int) {
        setUpMediaPlayer(index)
        mediaPlayer.setOnPreparedListener { playMusic() }
        mediaPlayer.setOnCompletionListener { onCompletion(index) }
        mediaPlayer.prepareAsync()
    }

    fun changeTrack(index: Int, adapter: MusicRecordAdapter) {
        setUpMediaPlayer(index)
        mediaPlayer.setOnPreparedListener {
            playMusic()
            adapter.updateCurrentTrack()
        }
        mediaPlayer.setOnCompletionListener { onCompletion(index, adapter) }
        mediaPlayer.prepareAsync()
    }


    fun playMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    fun resumeMusic(seekBar: SeekBar) {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(seekBar.progress)
            mediaPlayer.start()
        }
    }
}