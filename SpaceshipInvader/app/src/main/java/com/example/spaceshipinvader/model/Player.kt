package com.example.spaceshipinvader.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.spaceshipinvader.R

class Player(context: Context, maxScreenX: Float, maxScreenY: Float
) {
    private val minX = 0f
    private val minY = 0f

    var playerAvatar: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.player_avatar)
        private set

    private val maxX = maxScreenX - playerAvatar.width
    private val maxY = maxScreenY - playerAvatar.height

    var xCoordinate: Float = 75f
        private set

    var yCoordinate: Float = 50f
        private set

    var shape = Rect(xCoordinate.toInt(), yCoordinate.toInt(), playerAvatar.width, playerAvatar.height)
        private set


    fun updateX(newX: Float) {
        val updatedX = xCoordinate + newX
        if (updatedX in minX..maxX) {
            xCoordinate = updatedX
        } else if (updatedX < minX) {
            xCoordinate = minX
        } else
            xCoordinate = maxX

        shape.left = xCoordinate.toInt()
        shape.right = xCoordinate.toInt() + playerAvatar.width
    }

    fun updateY(newY: Float) {
        val updatedY = yCoordinate + newY
        if (updatedY in minY..maxY) {
            yCoordinate = updatedY
        } else if (updatedY < minY) {
            yCoordinate = minY
        } else {
            yCoordinate = maxY
        }

        shape.top = yCoordinate.toInt()
        shape.bottom = yCoordinate.toInt() + playerAvatar.height
    }

}