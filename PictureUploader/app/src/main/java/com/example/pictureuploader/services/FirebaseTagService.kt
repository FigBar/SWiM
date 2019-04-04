package com.example.pictureuploader.services


import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage



object FirebaseTagService : TagService {

    private val labeler = FirebaseVision.getInstance().onDeviceImageLabeler


    override fun generateTags(bitmap: Bitmap): Array<String> {
        var tagArray: Array<String> = arrayOf(" ")
        val fbImage = FirebaseVisionImage.fromBitmap(bitmap)
        labeler.processImage(fbImage)
            .addOnSuccessListener {
                tagArray = it.map { it.text }.toTypedArray()
            }
            .addOnFailureListener {
                tagArray = arrayOf("Error")
            }
        return tagArray
    }


}