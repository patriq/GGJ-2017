package com.theend.game.element

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.res.ResourceHandler

class StrumBarRing(val color: Color, val position: Vector2) {

    private companion object {
        private const val SIZE: Float = 25f
        private const val SCALE_INC: Float = 0.025f
        private const val ALPHA_DEC: Float = 0.025f
    }

    private val region: TextureRegion
    private var scale: Float
    private var alpha: Float
    var isRendering: Boolean

    init {
        region = ResourceHandler.getTexture("circle")
        isRendering = false
        scale = 1f
        alpha = 1f
    }

    fun update() {
        if (isRendering) {
            scale += SCALE_INC
            alpha -= ALPHA_DEC
            if (alpha <= 0) {
                isRendering = false
                scale = 1f
                alpha = 1f
            }
        }
    }

    fun render(batch: Batch) {
        if (isRendering) {
            batch.color = Color(color.r, color.g, color.b, alpha)
            batch.draw(region, position.x, position.y, SIZE/ 2, SIZE / 2f, SIZE, SIZE, scale, scale, 0f)
        }
    }

    fun dispose() {
        region.texture.dispose()
    }
}
