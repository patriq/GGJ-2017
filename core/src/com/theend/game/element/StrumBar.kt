package com.theend.game.element

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.res.ResourceHandler

class StrumBar(val position: Vector2, val width: Float, val height: Float) {

    private companion object {
        @JvmField val BAR_COLOR: Color = Color.RED
    }

    private val barRegion: TextureRegion

    init {
        barRegion = ResourceHandler.getTexture("quad")
    }

    fun update() {

    }

    fun render(batch: Batch) {
        batch.color = BAR_COLOR
        batch.draw(barRegion, position.x, position.y, width, height)
    }

    fun dispose() = barRegion.texture.dispose()
}
