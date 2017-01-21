package com.theend.game.element

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.theend.game.Game

class GuitarArm(world: World, renderer: ShapeRenderer) {

    companion object {
        @JvmField val FORCE: Vector2 = Vector2(1.5f, 0f)
        @JvmField val CHORD_COLORS: List<Color> = listOf(Color.RED, Color.GREEN, Color.PURPLE, Color.ORANGE)
        private const val NUM_CHORDS: Int = 4
        private const val CHORD_GAP: Float = 40f
        private const val ARM_Y: Float = 800f
    }

    val chords: MutableList<Chord>

    /** For calculus convenience. */
    private val startingX: Float

    init {
        chords = mutableListOf()
        startingX = (Game.WORLD_WIDTH - ((NUM_CHORDS * Chord.SUPPORT_SIZE) + (NUM_CHORDS - 1) * CHORD_GAP)) / 2f
        (0 until NUM_CHORDS).forEach {
            val x: Float = startingX + (it * Chord.SUPPORT_SIZE) + (it * CHORD_GAP)
            val y: Float = ARM_Y
            val chordPosition: Vector2 = Vector2(x, y)
            chords.add(Chord(world, renderer, chordPosition, CHORD_COLORS[it]))
        }
    }

    fun applyForceToChord(chordIndex: Int) {
        chords[chordIndex].middleSegment.applyForceToCenter(FORCE, true)
    }

    /**
     * Takes noteWidth to allow for more than 1 type of notes.
     *
     * @param chordIndex    ranges between [0, NUM_CHORDS - 1].
     * @param noteWidth     width of the note texture.
     */
    private fun getSpawnLoc(chordIndex: Int, noteWidth: Float): Vector2 {
        val x: Float = startingX + (chordIndex * Chord.SUPPORT_SIZE) + (chordIndex * CHORD_GAP) - noteWidth / 2f
        val y: Float = ARM_Y
        return Vector2(x, y)
    }

    /**
     * Spawns a [Note] at the given [Chord].
     *
     * @param chordIndex    ranges between [0, NUM_CHORDS - 1].
     * @param width         width of the note texture.
     * @param height        height of the note texture.
     */
    fun spawnNoteAtChord(chordIndex: Int, width: Float, height: Float) {
        val color: Color = chords[chordIndex].color
        val position: Vector2 = this.getSpawnLoc(chordIndex, width)
        chords[chordIndex].fallingNotes.add(Note(color, position, width, height))
    }

    fun update() {
        chords.forEach(Chord::update)
    }

    fun render(batch: Batch) {
        chords.forEach { it.render(batch) }
    }

    fun dispose() {
        chords.forEach(Chord::dispose)
    }
}
