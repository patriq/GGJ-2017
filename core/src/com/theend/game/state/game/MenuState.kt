package com.theend.game.state.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.theend.game.Game
import com.theend.game.res.ResourceHandler
import com.theend.game.state.StateAdapter
import com.theend.game.state.StateManager

/**
 * Created by xico | 22/01/2017.
 */
class MenuState(manager: StateManager) : StateAdapter(manager) {

    private val armRegion: TextureRegion
    private val title: TextureRegion
    private val menu: TextureRegion
    private val dot: TextureRegion

    private var currentOption: Int = 0
    private val maxOptions = 2

    private var x = 0f

    init {
        armRegion = ResourceHandler.getTexture("guitarArm")
        title = ResourceHandler.getTexture("title")
        menu = ResourceHandler.getTexture("menu")
        dot = ResourceHandler.getTexture("circle")
    }

    override fun update(delta: Float) {
        x += delta
    }

    override fun render(batch: Batch) {
        batch.projectionMatrix = manager.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        batch.draw(armRegion, 480f, -80f, 320f, 800f)
        batch.draw(title, (Game.WORLD_WIDTH / 2 - title.regionWidth / 2).toFloat(), Game.WORLD_HEIGHT * 2.5f / 4, title.regionWidth.toFloat(), title.regionHeight.toFloat())
        batch.draw(menu, (Game.WORLD_WIDTH / 2 - menu.regionWidth / 2).toFloat(), Game.WORLD_HEIGHT * 2.5f / 4 - 200, menu.regionWidth.toFloat(), menu.regionHeight.toFloat())
        batch.color = Color.BLACK
        for (i in Game.WORLD_WIDTH / 2 - menu.regionWidth / 5..Game.WORLD_WIDTH / 2 + menu.regionWidth / 5 - 1)
            batch.draw(dot, i.toFloat(), (Math.sin((10 * x + i).toDouble()) + Game.WORLD_HEIGHT * 2.5f / 4 - 200 + menu.regionHeight - 55.0 - (35 * currentOption).toDouble()).toFloat(), 1f, 1f)
        batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.DOWN)
            if (this.currentOption++ == maxOptions - 1) this.currentOption = 0
        if (keycode == Input.Keys.UP)
            if (this.currentOption-- == 0) this.currentOption = maxOptions - 1
        if (keycode == Input.Keys.ENTER) {
            if (currentOption == 0) manager.setState(PlayState(manager))
            if (currentOption == 1) Gdx.app.exit()
        }
        return true
    }

    override fun dispose() {

    }

    override fun resize(width: Int, height: Int) {

    }
}
