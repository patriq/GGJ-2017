package com.theend.game.element

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.res.ResourceHandler

class GameKeys() {

    private companion object {
        @JvmField val KEY_PATHS: Map<String, String> = mapOf(
                "key-A" to "key-A-pressed",
                "key-S" to "key-S-pressed",
                "key-K" to "key-K-pressed",
                "key-L" to "key-L-pressed"
        )
    }

    val keys: MutableList<SingleKey>

    init {
        keys = mutableListOf()
        var i: Int = 0
        for ((key, value) in KEY_PATHS) {
            keys.add(SingleKey(key, value))
            i++
        }
    }

    fun pressKey(keyIndex: Int) {
        keys[keyIndex].setKeyPressed()
    }

    fun unpressKey(keyIndex: Int) {
        keys[keyIndex].setKeyUnpressed()
    }

    fun render(batch: Batch) {
        keys.forEach { it.render(batch) }
    }

    class SingleKey(unpressedPath: String, pressedPath: String, val position: Vector2 = Vector2()) {

        private val pressedRegion: TextureRegion
        private val unpressedRegion: TextureRegion
        private var currentRegion: TextureRegion

        init {
            pressedRegion = ResourceHandler.getTexture(pressedPath)
            unpressedRegion = ResourceHandler.getTexture(unpressedPath)
            currentRegion = unpressedRegion
        }

        fun setKeyPressed() {
            this.currentRegion = pressedRegion
        }

        fun setKeyUnpressed() {
            this.currentRegion = unpressedRegion
        }

        fun render(batch: Batch) {
            batch.color = Color.WHITE
            batch.draw(currentRegion, position.x, position.y, 40f, 40f)
        }
    }
}
