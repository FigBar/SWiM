package com.example.spaceshipinvader.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import androidx.core.content.ContextCompat
import com.example.spaceshipinvader.R
import com.example.spaceshipinvader.model.InteractableElement
import com.example.spaceshipinvader.model.Player

class GameView(context: Context, private val maxX: Float, private val maxY: Float) : SurfaceView(context), Runnable {

    companion object {
        private const val SLEEP_FACTOR = 15
        private const val LIGHT_INTENSITY_CLASSIFIER = 150
        private const val COINS_AMOUNT = 6
        private const val GHOSTS_AMOUNT = 10
        private const val BACKGROUND_ELEMS_AMOUNT = 5
        private const val SCORE_LIVES_TEXT_SIZE = 60f
        private const val GAME_OVER_TEXT_SIZE = 150f
        private const val YOUR_SCORE_TEXT_SIZE = 100f
    }

    private val paint = Paint()
    private lateinit var canvas: Canvas
    private val surfaceHolder = holder

    private var collisionCounter = 3
    var pointsCounter = 0
    var isGameOver = false
        private set

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
        paint.color = Color.BLACK
        paint.textSize = GAME_OVER_TEXT_SIZE
        paint.textAlign = Paint.Align.CENTER
        val y = (canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2)
        canvas.drawText("GAME OVER", canvas.width / 2f, y, paint)
        paint.textSize = YOUR_SCORE_TEXT_SIZE
        canvas.drawText("SCORE: $pointsCounter", canvas.width / 2f, y + 100, paint)
        canvas.drawText("Tap to try again!", canvas.width / 2f, y + 250, paint)

    }

    private fun drawScoreAndLives() {
        paint.color = Color.WHITE
        paint.textSize = SCORE_LIVES_TEXT_SIZE
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Score: $pointsCounter", 200f,  60f, paint)
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
        for (i in 0..COINS_AMOUNT) {
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
        for (i in 0..GHOSTS_AMOUNT) {
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
        for (i in 0..BACKGROUND_ELEMS_AMOUNT) {
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
            if (lumens < LIGHT_INTENSITY_CLASSIFIER) {
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
            Thread.sleep(SLEEP_FACTOR.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun reload() {
        if(isGameOver) {
            isGameOver = false
            collisionCounter = 3
            pointsCounter = 0
            coinList.forEach { it.elementKill() }
            ghostList.forEach { it.elementKill() }
            backgroundElements.forEach { it.elementKill() }
            resume()
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