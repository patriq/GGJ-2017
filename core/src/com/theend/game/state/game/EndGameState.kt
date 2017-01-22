package com.theend.game.state.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.theend.game.Game
import com.theend.game.res.ResourceHandler
import com.theend.game.state.StateAdapter
import com.theend.game.state.StateManager


class EndGameState(result: Boolean, val player1Score : Int, val player2Score : Int, val misses : Int, manager: StateManager) : StateAdapter(manager) {

    private val armRegion: TextureRegion
    private val title: TextureRegion
    private val menu: TextureRegion
    private val dot: TextureRegion
    private val stats: TextureRegion
    private val font : BitmapFont
    private val layout : GlyphLayout

    private var currentOption: Int = 0
    private val maxOptions = 2

    private var x = 0f

    init {
        armRegion = ResourceHandler.getTexture("guitarArm")
        title = ResourceHandler.getTexture(if (result) "win-game" else "lose-game")
        menu = ResourceHandler.getTexture("end-menu")
        dot = ResourceHandler.getTexture("circle")
        stats = ResourceHandler.getTexture("end-stats")
        font = BitmapFont(Gdx.files.internal("fonts/dual.fnt")).apply {
            color = Color.BLACK
        }
        layout = GlyphLayout()
        layout.setText(font, "Player 1: $player1Score\nPlayer 2: $player2Score\nMisses: $misses")

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

        font.draw(batch, layout, (Game.WORLD_WIDTH / 2 - layout.width / 2), Game.WORLD_HEIGHT * 2.5f / 4 - 20)

        batch.draw(menu, (Game.WORLD_WIDTH / 2 - menu.regionWidth / 2).toFloat(), Game.WORLD_HEIGHT * 2.5f / 4 - 250, menu.regionWidth.toFloat(), menu.regionHeight.toFloat())
        batch.color = Color.BLACK
        val divisor : Float = if(currentOption == 0) 2.6f else 5f
        for (i in Game.WORLD_WIDTH / 2 - (menu.regionWidth / divisor).toInt() until Game.WORLD_WIDTH / 2 + (menu.regionWidth / divisor).toInt())
            batch.draw(dot, i.toFloat(), (Math.sin((10 * x + i).toDouble()) + Game.WORLD_HEIGHT * 2.5f / 4 - 250 + menu.regionHeight - 55.0 - (35 * currentOption).toDouble()).toFloat(), 1f, 1f)
        batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.DOWN)
            if (this.currentOption++ == maxOptions - 1) this.currentOption = 0
        if (keycode == Input.Keys.UP)
            if (this.currentOption-- == 0) this.currentOption = maxOptions - 1
        if (keycode == Input.Keys.ENTER) {
            if (currentOption == 0) manager.setState(PlayState(manager))
            if (currentOption == 1) manager.setState(MenuState(manager))
        }
        return true
    }

    override fun dispose() {

    }

    override fun resize(width: Int, height: Int) {

    }

}