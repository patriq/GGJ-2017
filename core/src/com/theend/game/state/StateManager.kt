package com.theend.game.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.theend.game.Game

class StateManager {

    private var currentState: State? = null

    val camera: Camera
    val viewport: Viewport

    init {
        camera = OrthographicCamera(Game.WORLD_WIDTH.toFloat(), Game.WORLD_HEIGHT.toFloat())
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        viewport = ExtendViewport(Game.WORLD_WIDTH.toFloat(), Game.WORLD_HEIGHT.toFloat(), camera)
    }

    internal fun setState(state: State) {
        currentState?.dispose()
        currentState = state
        Gdx.input.inputProcessor = currentState
    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        currentState?.resize(width, height)
    }

    internal fun updateState(delta: Float) = currentState?.update(delta)

    internal fun renderState(batch: Batch) = currentState?.render(batch)

    internal fun dispose() = currentState?.dispose()
}
