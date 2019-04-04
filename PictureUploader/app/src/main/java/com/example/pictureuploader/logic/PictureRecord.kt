package com.example.pictureuploader.logic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PictureRecord(val url: String, val title: String, val date: Calendar) : Parcelable