package com.example.musicplayer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MusicRecord(val path: String, val title: String, val artist: String) : Parcelable
