package com.theend.game.juice

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import java.util.*

/**
 * Each [Chord] will have one juice shape manager associated, that will render the [JuicyShape]s
 * of the according color, and at the according position.
 */
class JuicyShapeManager(color: Color, numShapes: Int) {

    private companion object PossibleShapes {
        @JvmField val SHAPES: List<String> = listOf("note-1", "note-2")
        private const val MAX_SETS: Int = 3
    }

    /** Maps a set of shapes to a boolean 'is active'. */
    private val shapeSets: MutableMap<MutableList<JuicyShape>, Boolean>

    private val random: Random

    init {
        random = Random()
        shapeSets = mutableMapOf()
        for (i in 0 until MAX_SETS) {
            val set: MutableList<JuicyShape> = mutableListOf()
            for (j in 0 until numShapes) {
                set.add(JuicyShape(pickRandomShape(), color, Vector2()))
            }
            shapeSets.put(set, false)
        }
    }

    private fun pickRandomShape(): String {
        return SHAPES[random.nextInt(SHAPES.size)]
    }

    fun blastAroundCenter(center: Vector2) {
        for ((key, value) in shapeSets) {
            if (!value) {
                shapeSets.put(key, true)
                key.activate(center)
                return
            }
        }
    }

    private fun MutableList<JuicyShape>.activate(center: Vector2) {
        this.forEach {
            it.shouldBeRendered = true
            val randomX: Float = center.x + random.nextInt(4) - 2
            val randomY: Float = center.y + random.nextInt(700) - 350
            it.position.set(randomX, randomY)
        }
    }

    fun update() {
        for ((key, value) in shapeSets) {
            key.forEach { it.update() }
            if (key.filterNot { it.shouldBeRendered }.isNotEmpty()) {
                shapeSets.put(key, false)
            }
        }
    }

    fun render(batch: Batch) = shapeSets.forEach { it.key.forEach { it.render(batch) } }

    fun dispose() = shapeSets.keys.forEach { it.forEach { it.dispose() } }
}