package com.example.spaceshipinvader

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spaceshipinvader.views.GameView

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var lightSensor: Sensor
    private lateinit var gameView: GameView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST)
        val screenSize = Point()
        windowManager.defaultDisplay.getSize(screenSize)
        gameView = GameView(this, screenSize.y.toFloat(), screenSize.x.toFloat())
        setContentView(gameView)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER)
                gameView.updatePlayerCoordinates(event.values[0], event.values[1])
            else
                gameView.reactToLightChange(event.values[0])
        }
    }


    override fun onPause() {
        super.onPause()
        gameView.pause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()

    }

}
