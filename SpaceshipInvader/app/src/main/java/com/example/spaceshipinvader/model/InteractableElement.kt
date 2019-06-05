package com.example.spaceshipinvader.model
import android.graphics.Bitmap

import android.graphics.Rect
import java.util.*

class InteractableElement(maxScreenX: Float, maxScreenY: Float, var avatar: Bitmap) {

    companion object {
        private const val SPEED_RANGE = 12
        private const val KILL_SPEED_RANGE = 6
        private const val SPEED_OFFSET = 5
        private const val INITIAL_Y_AXIS_OFFSET_RANGE = 1500
        private const val KILL_Y_AXIS_OFFSET_RANGE = 1000f
    }


    private val randomizer = Random()
    private var motionSpeed = randomizer.nextInt(SPEED_RANGE) + SPEED_OFFSET


    private val maxX = maxScreenX - avatar.width
    private val maxY = maxScreenY - avatar.height

    var xCoordinate: Float = randomizer.nextInt(maxX.toInt()).toFloat()
        private set

    var yCoordinate: Float = maxY + randomizer.nextInt(
        INITIAL_Y_AXIS_OFFSET_RANGE).toFloat()
        private set

    var shape = Rect(xCoordinate.toInt(), yCoordinate.toInt(), avatar.width, avatar.height)
        private set


    fun updateCoordinates() {
        yCoordinate -= motionSpeed
        if(yCoordinate + avatar.height < 0){
            motionSpeed = randomizer.nextInt(SPEED_RANGE) + SPEED_OFFSET
            xCoordinate = randomizer.nextInt(maxX.toInt()).toFloat()
            yCoordinate = maxY
        }

        shape.left = xCoordinate.toInt()
        shape.top = yCoordinate.toInt()
        shape.right = xCoordinate.toInt() + avatar.width
        shape.bottom = yCoordinate.toInt() + avatar.height
    }

    fun elementKill() {
        motionSpeed = randomizer.nextInt(SPEED_RANGE) + SPEED_OFFSET
        xCoordinate = randomizer.nextInt(maxX.toInt()).toFloat()
        yCoordinate = maxY + KILL_Y_AXIS_OFFSET_RANGE
    }
}