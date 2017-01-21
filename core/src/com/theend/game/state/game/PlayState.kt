package com.theend.game.state.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.theend.game.Game
import com.theend.game.b2d.PhysicsWorld
import com.theend.game.element.GuitarArm
import com.theend.game.element.StrumBar
import com.theend.game.state.StateAdapter
import com.theend.game.state.StateManager
import java.util.*

class PlayState(manager: StateManager) : StateAdapter(manager) {

    private val physicsWorld: PhysicsWorld
    private val shapeRenderer: ShapeRenderer
    private val guitarArm: GuitarArm
    private val strumBar: StrumBar

    private val random: Random

    init {
        physicsWorld = PhysicsWorld().apply { this.debugging = false }
        shapeRenderer = ShapeRenderer()
        guitarArm = GuitarArm(physicsWorld.world, shapeRenderer)
        strumBar = StrumBar(Vector2(Game.WORLD_WIDTH / 2f - 165f, 70f), 300f, 20f)
        random = Random()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A -> checkChordInteractions(0)
            Input.Keys.S -> checkChordInteractions(1)
            Input.Keys.D -> checkChordInteractions(2)
            Input.Keys.F -> checkChordInteractions(3)
            Input.Keys.M -> guitarArm.spawnNoteAtChord(random.nextInt(4), 40f, 40f)
        }
        return true
    }

    private fun checkChordInteractions(chordIndex: Int) {
        guitarArm.applyForceToChord(chordIndex)
        if (guitarArm.chords[chordIndex].fallingNotes.isEmpty()) return
        guitarArm.chords[chordIndex].fallingNotes.first().apply {
            this.startImploding()
        }
    }

    override fun resize(width: Int, height: Int) {
        physicsWorld.resize(width, height)
        shapeRenderer.projectionMatrix = manager.camera.combined
    }

    override fun update(delta: Float) {
        physicsWorld.update()
        guitarArm.update()
        strumBar.update()
        println(Gdx.graphics.framesPerSecond)
    }

    override fun render(batch: Batch) {
        physicsWorld.render()
        batch.projectionMatrix = manager.camera.combined
        batch.begin()
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
