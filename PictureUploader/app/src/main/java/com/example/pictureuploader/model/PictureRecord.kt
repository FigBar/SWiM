package com.example.pictureuploader.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class PictureRecord(
    val url: String,
    val title: String,
    val date: Calendar,
    val tags: ArrayList<String> = ArrayList()
) : Parcelable {

    fun assessSimilarity(compareWith: PictureRecord): Int =
        if (!compareWith.tags.isEmpty()) 0 else compareWith.tags.filter { tags.contains(it) }.count()
}
