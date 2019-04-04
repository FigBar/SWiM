package com.example.pictureuploader.services

import android.graphics.Bitmap

interface TagService {
    fun generateTags(bitmap: Bitmap): Array<String>
}