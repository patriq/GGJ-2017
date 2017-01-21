package com.theend.game.state

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

/**
 * Convenience implementation of the [State]. Pick whatever you want.
 */

abstract class StateAdapter protected constructor(val manager: StateManager) : State {

    override abstract fun update(delta: Float)

    override abstract fun render(batch: Batch)

    override abstract fun dispose()

    override abstract fun resize(width: Int, height: Int)

    override fun unproject(screenX: Float, screenY: Float): Vector2 {
        return Vector2() // TODO
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }
}
