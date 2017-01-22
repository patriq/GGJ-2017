package com.theend.game.element

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.res.ResourceHandler

class StrumBar(val position: Vector2, val width: Float, val height: Float) {

    private companion object {
        @JvmField val BAR_COLOR: Color = Color.DARK_GRAY
    }

    private val barRegion: TextureRegion
    private val rings: MutableList<StrumBarRing>

    init {
        barRegion = ResourceHandler.getTexture("quad")
        rings = mutableListOf()
    }

    fun drawColorRing(color: Color, position: Vector2) {
        rings.add(StrumBarRing(color, position).apply { this.isRendering = true })
    }

    fun update() {
        val toDelete: MutableList<StrumBarRing> = mutableListOf()
        for (i in 0 until rings.size) {
            rings[i].update()
            if (!rings[i].isRendering) {
                toDelete.add(rings[i])
            }
        }
        /** Don't keep the rings list too heavy! */
        rings.removeAll(toDelete)
    }

    fun render(batch: Batch) {
        batch.color = BAR_COLOR
        batch.draw(barRegion, position.x, position.y, width, height)
        rings.forEach { it.render(batch) }
    }

    fun dispose() {
        barRegion.texture.dispose()
        rings.forEach { it.dispose() }
    }
}
