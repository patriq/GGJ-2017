package com.theend.game.state

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

interface State : InputProcessor {

    fun update(delta: Float)

    fun render(batch: Batch)

    fun resize(width: Int, height: Int)

    fun dispose()

    fun unproject(screenX: Float, screenY: Float): Vector2

}
