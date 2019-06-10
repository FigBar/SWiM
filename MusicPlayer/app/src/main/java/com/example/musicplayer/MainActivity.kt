package com.example.musicplayer

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.model.MusicRecord
import com.example.musicplayer.recycler_view.MusicRecordAdapter
import com.example.musicplayer.repositories.MusicTracksRepository
import com.example.musicplayer.services.MediaPlayerService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val musicRepository = MusicTracksRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MusicRecordAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        loadMusicRecords()
        viewManager = LinearLayoutManager(this)
        viewAdapter = MusicRecordAdapter(viewManager)
        recyclerView = main_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(100)
        MediaPlayerService.setMusicRecordAdapter(viewAdapter)
    }

    private fun loadMusicRecords() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.DURATION
        )
        val cursor = contentResolver.query(
            uri,
            projection,
            MediaStore.Audio.Media.IS_MUSIC + " != 0",
            null,
            MediaStore.Audio.Media.TITLE + " ASC"
        )
        if (cursor != null){
            while(cursor.moveToNext()) {
                val path = cursor.getString(0)
                val title = cursor.getString(1)
                val artist = cursor.getString(2)
                val duration = cursor.getInt(3)
                Log.d("path", "path : $path")
                Log.d("name", "name : $title")
                Log.d("artist", "artist: $artist")
                musicRepository.musicRecordsList.add(MusicRecord(path, title, artist, duration))
            }
            cursor.close()
        }
    }

    override fun onResume() {
        super.onResume()
        val adapter = recyclerView.adapter as MusicRecordAdapter
        adapter.updateCurrentTrack()
    }
}
