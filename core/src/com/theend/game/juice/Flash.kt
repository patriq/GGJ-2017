package com.theend.game.juice

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.res.ResourceHandler

class Flash(val flashColor: Color, val position: Vector2, val width: Float, val height: Float) {

    private companion object {
        private const val INITIAL_ALPHA: Float = 0.4f
        private const val ALPHA_DEC: Float = 0.0025f
    }

    private val region: TextureRegion

    private var isFlashing: Boolean
    private var alpha: Float

    init {
        region = ResourceHandler.getTexture("quad")
        isFlashing = false
        alpha = INITIAL_ALPHA
    }

    fun flash() {
        alpha = INITIAL_ALPHA
        isFlashing = true
    }

    fun update() {
        if (isFlashing) {
            alpha -= ALPHA_DEC
            if (alpha <= 0f) {
                isFlashing = false
                alpha = INITIAL_ALPHA
            }
        }
    }

    fun render(batch: Batch) {
        if (isFlashing) {
            batch.color = Color(flashColor.r, flashColor.g, flashColor.b, alpha)
            batch.draw(region, position.x, position.y, width, height)
        }
    }
}