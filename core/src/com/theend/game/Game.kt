package com.theend.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.theend.game.res.ResourceHandler
import com.theend.game.state.StateManager
import com.theend.game.state.game.PlayState

class Game : ApplicationAdapter() {

    companion object {
        const val WORLD_WIDTH: Int = 1280
        const val WORLD_HEIGHT: Int = 720
        const val TITLE: String = "Waves 'n Hoes"
    }

    lateinit internal var manager: StateManager
    lateinit internal var batch: Batch

    override fun create() {
        ResourceHandler.init("pack.pack")
        batch = SpriteBatch()
        manager = StateManager()
        manager.setState(PlayState(manager))
    }

    override fun resize(width: Int, height: Int) {
        manager.resize(width, height)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        manager.updateState(Gdx.graphics.deltaTime)
        manager.renderState(batch)
    }

    override fun dispose() {
        manager.dispose()
    }
}