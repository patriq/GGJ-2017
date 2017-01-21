package com.theend.game.juice

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.res.ResourceHandler

class JuicyShape(textureName: String, val color: Color, val position: Vector2) {

    private companion object MagicValue {
        private const val ANGLE_RANDOM_MULT: Float = 50f
        private const val ANGLE_INC: Float = 0.1f
        private const val ALPHA_DEC: Float = 0.0025f
        /** Size will vary between 7 and 12, including both. */
        @JvmField val SIZE_RANGE: FloatArray = floatArrayOf(7f, 8f, 9f, 10f, 11f, 12f)
    }

    private val region: TextureRegion
    private val size: Float
    private var angle: Float
    private var alpha: Float

    private var randomXIncrement: Float
    private var randomYIncrement: Float

    var shouldBeRendered: Boolean
    private var oldBatchColor: Color?

    init {
        region = ResourceHandler.getTexture(textureName)
        angle = Math.random().toFloat() * ANGLE_RANDOM_MULT
        size = pickRandomSize()
        alpha = 1f
        randomXIncrement = Math.random().toFloat() + 0.1f
        /* Change the direction on the X axis!. */
        if (Math.random() <= 0.5) randomXIncrement *= -1f
        randomYIncrement = Math.random().toFloat() / 2f + 0.05f
        shouldBeRendered = false
        oldBatchColor = null
    }

    private fun pickRandomSize(): Float {
        val range: Int = (SIZE_RANGE.last().toInt() - SIZE_RANGE.first().toInt()) + 1
        return (Math.random().toFloat() * range) + SIZE_RANGE.first().toInt()
    }

    fun update() {
        /* Keep this outside the if statement for a larger explosion. */
        position.add(randomXIncrement, randomYIncrement)
        if (shouldBeRendered) {
            angle += ANGLE_INC
            alpha -= ALPHA_DEC
            if (alpha <= 0f) {
                alpha = 1f
                shouldBeRendered = false
            }
        }
    }

    fun render(batch: Batch) {
        if (shouldBeRendered) {
            oldBatchColor = batch.color
            batch.color = Color(color.r, color.g, color.b, alpha)
            batch.draw(region, position.x, position.y, size / 2f, size / 2f, size, size, 1f, 1f, angle)
            batch.color = oldBatchColor
        }
    }

    fun dispose() {
        region.texture.dispose()
    }
}
