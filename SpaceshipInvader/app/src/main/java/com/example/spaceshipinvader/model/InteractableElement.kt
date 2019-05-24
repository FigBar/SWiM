package com.example.spaceshipinvader.model

import android.content.Context
import android.graphics.Bitmap

import android.graphics.Rect
import java.util.*

class InteractableElement(maxScreenX: Float, maxScreenY: Float, var avatar: Bitmap) {


    private val randomizer = Random()
    private var motionSpeed = randomizer.nextInt(15) + 5


    private val maxX = maxScreenX - avatar.width
    private val maxY = maxScreenY - avatar.height

    var xCoordinate: Float = randomizer.nextInt(maxX.toInt()).toFloat()
        private set

    var yCoordinate: Float = maxY + randomizer.nextInt(1000).toFloat()
        private set

    var shape = Rect(xCoordinate.toInt(), yCoordinate.toInt(), avatar.width, avatar.height)
        private set


    fun updateCoordinates() {
        yCoordinate -= motionSpeed
        if(yCoordinate + avatar.height < 0){
            motionSpeed = randomizer.nextInt(6) + 5
            xCoordinate = randomizer.nextInt(maxX.toInt()).toFloat()
            yCoordinate = maxY
        }

        shape.left = xCoordinate.toInt()
        shape.top = yCoordinate.toInt()
        shape.right = xCoordinate.toInt() + avatar.width
        shape.bottom = yCoordinate.toInt() + avatar.height
    }

    fun elementKill() {
        motionSpeed = randomizer.nextInt(6) + 5
        xCoordinate = randomizer.nextInt(maxX.toInt()).toFloat()
        yCoordinate = maxY + 1000f
    }
}