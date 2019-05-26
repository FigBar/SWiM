package com.example.spaceshipinvader.views

import android.content.Context
import android.graphics.*
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.example.spaceshipinvader.R
import com.example.spaceshipinvader.model.InteractableElement
import com.example.spaceshipinvader.model.Player

class GameView(context: Context, private val maxX: Float, private val maxY: Float) : SurfaceView(context), Runnable {

    private val paint = Paint()
    private lateinit var canvas: Canvas
    private val surfaceHolder = holder

    private var collisionCounter = 3
    var pointsCounter = 0
    private var isGameOver = false

    @Volatile
    internal var currentlyPlaying: Boolean = false
    private var gameThread: Thread? = null
    private val playerInstance = Player(context, maxX, maxY)
    private val ghostList = mutableListOf<InteractableElement>()
    private val coinList = mutableListOf<InteractableElement>()
    private val backgroundElements = mutableListOf<InteractableElement>()


    init {
        generateBackgroundElements()
        generateMonsterList()
        generateCoins()
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)
            canvas.drawColor(ContextCompat.getColor(context, R.color.skyColor))
            drawInteractableElements(backgroundElements)
            drawPlayer()
            drawInteractableElements(ghostList)
            drawInteractableElements(coinList)
            drawScoreAndLives()
            if (isGameOver) {
                drawGameOver()
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawGameOver() {
        paint.color = Color.WHITE
        paint.textSize = 150f
        paint.textAlign = Paint.Align.CENTER
        val y = (canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2)
        canvas.drawText("GAME OVER", canvas.width / 2f, y, paint)

    }

    private fun drawScoreAndLives() {
        paint.color = Color.WHITE
        paint.textSize = 60f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Score: $pointsCounter", 200f, 60f, paint)
        canvas.drawText("Lives: $collisionCounter", 500f, 60f, paint)
    }

    private fun drawPlayer() {
        canvas.drawBitmap(
            playerInstance.playerAvatar,
            playerInstance.yCoordinate,
            playerInstance.xCoordinate,
            paint
        )
    }

    private fun drawInteractableElements(list: MutableList<InteractableElement>) {
        list.forEach {
            canvas.drawBitmap(
                it.avatar,
                it.yCoordinate,
                it.xCoordinate,
                paint
            )
        }
    }

    override fun run() {
        while (currentlyPlaying) {
            motion()
            draw()
            control()
        }
    }

    private fun generateCoins() {
        for (i in 0..6) {
            coinList.add(
                InteractableElement(
                    maxX,
                    maxY,
                    BitmapFactory.decodeResource(context.resources, R.drawable.coin_icon)
                )
            )
        }
    }

    private fun generateMonsterList() {
        for (i in 0..10) {
            ghostList.add(
                InteractableElement(
                    maxX,
                    maxY,
                    BitmapFactory.decodeResource(context.resources, R.drawable.ghost_avatar)
                )
            )
        }
    }

    private fun generateBackgroundElements() {
        for (i in 0..5) {
            backgroundElements.add(
                InteractableElement(
                    maxX,
                    maxY,
                    BitmapFactory.decodeResource(context.resources, R.drawable.cloud_icon)
                )
            )

        }
    }

    fun reactToLightChange(lumens: Float) {
        try {
            if (lumens < 150) {
                ghostList.forEach {
                    it.avatar = BitmapFactory.decodeResource(context.resources, R.drawable.light_changed_ghost_icon)
                }
            } else {
                ghostList.forEach {
                    it.avatar = BitmapFactory.decodeResource(context.resources, R.drawable.ghost_avatar)
                }
            }
        } catch (exc: ConcurrentModificationException) {
            exc.printStackTrace()
        }
    }

    private fun motion() {
        ghostList.forEach {
            it.updateCoordinates()
            if (Rect.intersects(playerInstance.shape, it.shape)) {
                it.elementKill()
                collisionCounter--
            }

        }
        if (collisionCounter == 0) {
            currentlyPlaying = false
            isGameOver = true
        }
        coinList.forEach {
            it.updateCoordinates()
            if (Rect.intersects(playerInstance.shape, it.shape)) {
                it.elementKill()
                pointsCounter++
            }
        }
        backgroundElements.forEach {
            it.updateCoordinates()
        }
    }

    private fun control() {
        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun updatePlayerCoordinates(newX: Float, newY: Float) {
        playerInstance.updateX(newX)
        playerInstance.updateY(newY)
    }

    fun pause() {
        currentlyPlaying = false
        try {
            gameThread!!.join()
        } catch (e: InterruptedException) {
        }
    }

    fun resume() {
        currentlyPlaying = true
        gameThread = Thread(this)
        gameThread!!.start()
    }


}