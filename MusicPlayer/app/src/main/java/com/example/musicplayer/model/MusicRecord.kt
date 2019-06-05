package com.example.musicplayer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MusicRecord(val uri: Uri, val title: String, val artist: String, val length: Double) : Parcelable
