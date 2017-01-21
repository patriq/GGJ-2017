package com.theend.game.state.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.theend.game.Game
import com.theend.game.b2d.PhysicsWorld
import com.theend.game.core.data.song.*
import com.theend.game.element.GuitarArm
import com.theend.game.element.StrumBar
import com.theend.game.state.StateAdapter
import com.theend.game.state.StateManager
import java.util.*

class PlayState(manager: StateManager) : StateAdapter(manager), BeatListener, WarnListener {

    private val physicsWorld: PhysicsWorld
    private val shapeRenderer: ShapeRenderer
    private val guitarArm: GuitarArm
    private val strumBar: StrumBar

    private val random: Random

    private val song: Song

    init {
        physicsWorld = PhysicsWorld().apply { this.debugging = false }
        shapeRenderer = ShapeRenderer()
        guitarArm = GuitarArm(physicsWorld.world, shapeRenderer)
        strumBar = StrumBar(Vector2(Game.WORLD_WIDTH / 2f - 165f, 70f), 300f, 20f)
        random = Random()

        song = Song("metro")
        song.addBeatListener(this)
        song.addWarnListener(this)
        song.play()
    }


    val pressTimes: MutableList<Beat> = mutableListOf()
    val beatTimes: MutableList<BeatEvent> = mutableListOf()
    override fun keyDown(keycode: Int): Boolean {
        var chord = -1
        when (keycode) {
            Input.Keys.A -> chord = 0
            Input.Keys.S -> chord = 1
            Input.Keys.D -> chord = 2
            Input.Keys.F -> chord = 3
        }
        if (chord == -1) return false
        pressTimes.add(Beat(song.position.toInt(), 0, chord))
        checkChordInteractions(chord)
        return true
    }

    override fun onWarn(event: BeatEvent) {
        guitarArm.spawnNoteAtChord(event.beat.chord, 40f, 40f, event.beat)
    }

    override fun onBeat(event: BeatEvent) {
        println("Beat!")
        beatTimes.add(event)
    }

    private fun checkChordInteractions(chordIndex: Int) {
        guitarArm.applyForceToChord(chordIndex)
        if (guitarArm.chords[chordIndex].fallingNotes.isEmpty()) return
        guitarArm.activateJuicyShapesAtChord(chordIndex)
    }

    override fun resize(width: Int, height: Int) {
        physicsWorld.resize(width, height)
        shapeRenderer.projectionMatrix = manager.camera.combined
    }

    override fun update(delta: Float) {
        physicsWorld.update()
        guitarArm.update()
        strumBar.update()
        song.update(delta)
        pressTimes.removeAll { song.position - it.beatPosition > 800 }
        beatTimes.removeAll { song.position - it.musicPosition > 800 }
        val toDelete: MutableList<Beat> = mutableListOf()
        for (pressTime in pressTimes) {
            for (beatTime in beatTimes) {
                if (Math.abs(pressTime.beatPosition - beatTime.musicPosition) < 50 && pressTime.chord == beatTime.beat.chord) {
                    println("Ya!")
                    toDelete.add(pressTime)
                    beatTimes.remove(beatTime)
                    guitarArm.chords[pressTime.chord].apply { this.fallingNotes.first().startImploding() }
                    break
                }
            }
        }
        pressTimes.removeAll(toDelete)
        for (chord in guitarArm.chords)
            for (note in chord.fallingNotes)
                note.position.y = strumBar.position.y + strumBar.height - 0.5f + ((note.beat.beatPosition - song.position) / song.warnMilliseconds) * (Game.WORLD_HEIGHT - strumBar.position.y - strumBar.height)
    }

    override fun render(batch: Batch) {
        physicsWorld.render()
        batch.projectionMatrix = manager.camera.combined
        batch.begin()
        guitarArm.renderArmRegion(batch)
        strumBar.render(batch)
        guitarArm.render(batch)
        batch.end()
    }

    override fun dispose() {
        shapeRenderer.dispose()
        guitarArm.dispose()
        strumBar.dispose()
    }
}
