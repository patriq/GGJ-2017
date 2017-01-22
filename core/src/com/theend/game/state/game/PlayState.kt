package com.theend.game.state.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.theend.game.Game
import com.theend.game.b2d.PhysicsWorld
import com.theend.game.b2d.toPixels
import com.theend.game.core.data.song.*
import com.theend.game.element.GameKeys
import com.theend.game.element.GuitarArm
import com.theend.game.element.Note
import com.theend.game.element.StrumBar
import com.theend.game.juice.Flash
import com.theend.game.state.StateAdapter
import com.theend.game.state.StateManager
import com.theend.game.ui.ScoreDisplay
import java.util.*

class PlayState(manager: StateManager) : StateAdapter(manager), BeatListener, WarnListener {

    private val visualLag = 10f
    private val soundLag = 30f

    private val physicsWorld: PhysicsWorld
    private val shapeRenderer: ShapeRenderer
    private val guitarArm: GuitarArm
    private val strumBar: StrumBar

    private val random: Random
    private val scoreDisplay: ScoreDisplay
    private val flashes: MutableList<Flash>

    private val song: Song

    private val gameKeys: GameKeys

    private val failSound: Sound

    private var successfulRun: Boolean

    init {
        physicsWorld = PhysicsWorld()
        shapeRenderer = ShapeRenderer()
        guitarArm = GuitarArm(physicsWorld.world, shapeRenderer)
        strumBar = StrumBar(Vector2(Game.WORLD_WIDTH / 2f - ((guitarArm.armRegionWidth - 10f) / 2f), 90f), guitarArm.armRegionWidth - 10f, 20f)
        random = Random()
        scoreDisplay = ScoreDisplay()

        flashes = mutableListOf()
        val flashWidth: Float = (Game.WORLD_WIDTH - guitarArm.armRegionWidth) / 4f
        (0..3).forEach {
            var x: Float = (it * flashWidth)
            val y: Float = 0f
            var defWidth = flashWidth;
            if (it == 1 || it == 2) defWidth += 20
            if (it == 2) x -= 20
            if (it >= 2) {
                x += guitarArm.armRegionWidth
            }
            flashes.add(Flash(GuitarArm.CHORD_COLORS[it], Vector2(x, y), defWidth, 720f))
        }

        gameKeys = GameKeys().apply {
            (0 until guitarArm.chords.size).forEach {
                this.keys[it].position.set(guitarArm.chords[it].topSupport.position.toPixels().x - 20f, 10f)
            }
        }

        failSound = Gdx.audio.newSound(Gdx.files.internal("sounds/falha.ogg"))
        successfulRun = true

        song = Song("extremes")
        song.setOnCompletionListener {
            manager.setState(EndGameState(successfulRun, scoreDisplay.score1, scoreDisplay.score2, scoreDisplay.misses, manager))
        }
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
            Input.Keys.K -> chord = 2
            Input.Keys.L -> chord = 3
        }
        if (chord == -1) return false
        pressTimes.add(Beat(song.position.toInt(), 0, chord))
        gameKeys.pressKey(chord)
        checkChordInteractions(chord)

        if (guitarArm.chords[chord].fallingNotes.isEmpty()) return false
        val closestNote: Note = guitarArm.chords[chord].fallingNotes.sortedBy { Math.abs(song.position - it.beat.beatPosition) }.first()
        if (Math.abs(song.position - soundLag - closestNote.beat.beatPosition) > 150) {
            failSound.play()
            if (++scoreDisplay.misses >= 30f) {
                song.fade()
                successfulRun = false
            }
            updateScore(chord, -100)
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        var chord = -1
        when (keycode) {
            Input.Keys.A -> chord = 0
            Input.Keys.S -> chord = 1
            Input.Keys.K -> chord = 2
            Input.Keys.L -> chord = 3
        }
        if (chord == -1) return false
        gameKeys.unpressKey(chord)
        return true
    }

    override fun onWarn(event: BeatEvent) {
        guitarArm.spawnNoteAtChord(event.beat.chord, 40f, 40f, event.beat)
    }

    override fun onBeat(event: BeatEvent) {
        beatTimes.add(event)
    }

    private fun checkChordInteractions(chordIndex: Int) {
        guitarArm.applyForceToChord(chordIndex)
    }

    override fun resize(width: Int, height: Int) {
        physicsWorld.resize(width, height)
        shapeRenderer.projectionMatrix = manager.camera.combined
    }

    private fun updateScore(chordIndex: Int, score: Int) {
        if (chordIndex == 0 || chordIndex == 1)
            scoreDisplay.score1 += score
        else
            scoreDisplay.score2 += score
    }

    override fun update(delta: Float) {
        physicsWorld.update()
        guitarArm.update()
        flashes.forEach(Flash::update)
        strumBar.update()
        song.update(delta)

        pressTimes.removeAll { song.position - it.beatPosition > 300 }

        beatTimes.removeAll { song.position - it.musicPosition > 800 }
        val toDelete: MutableList<Beat> = mutableListOf()
        for (pressTime in pressTimes) {
            for (beatTime in beatTimes) {
                val diff = Math.abs(pressTime.beatPosition - soundLag - beatTime.musicPosition)
                if (pressTime.chord == beatTime.beat.chord && diff <= 150) {
                    toDelete.add(pressTime)
                    beatTimes.remove(beatTime)
                    guitarArm.chords[pressTime.chord].apply { this.fallingNotes.first().startImploding() }
                    flashes[pressTime.chord].flash()
                    strumBar.drawColorRing(
                            guitarArm.chords[pressTime.chord].originalColor,
                            Vector2(guitarArm.chords[pressTime.chord].topSupport.position.toPixels().x - 10f, strumBar.position.y)
                    )
                    guitarArm.activateJuicyShapesAtChord(pressTime.chord)
                    updateScore(pressTime.chord, (-0.1f * diff + 40).toInt())
                    break
                }
            }
        }
        pressTimes.removeAll(toDelete)
        for (chord in guitarArm.chords)
            for (note in chord.fallingNotes)
                note.position.y = (strumBar.position.y + strumBar.height) +
                        visualLag + ((note.beat.beatPosition - song.position) / song.warnMilliseconds) * (Game.WORLD_HEIGHT - strumBar.position.y - strumBar.height)
    }

    override fun render(batch: Batch) {
        batch.projectionMatrix = manager.camera.combined
        batch.begin()
        flashes.forEach { it.render(batch) }
        guitarArm.renderArmRegion(batch)
        strumBar.render(batch)
        guitarArm.render(batch)
        gameKeys.render(batch)
        scoreDisplay.render(batch)
        batch.end()
        physicsWorld.render()
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}
