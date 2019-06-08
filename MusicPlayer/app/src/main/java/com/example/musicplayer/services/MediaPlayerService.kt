package com.example.musicplayer.services

import android.media.MediaPlayer
import android.widget.SeekBar
import com.example.musicplayer.PlayerActivity
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

    fun onCompletion(index: Int, player: PlayerActivity) {
        val nextTrackIndex = (index + 1) % MusicTracksRepository.musicRecordsList.size
        player.currentTrackNumber += 1
        changeTrack(nextTrackIndex, player)
        player.prepareNewSong()
    }

    private fun onCompletion(index: Int, adapter: MusicRecordAdapter) {
        val nextTrackIndex = (index + 1) % MusicTracksRepository.musicRecordsList.size
        changeTrack(nextTrackIndex, adapter)
    }

    fun changeTrack(index: Int, player: PlayerActivity) {
        setUpMediaPlayer(index)
        mediaPlayer.setOnPreparedListener { playMusic() }
        mediaPlayer.setOnCompletionListener { onCompletion(index, player) }
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