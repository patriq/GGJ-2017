package com.theend.game.b2d

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

class PhysicsWorld {

    companion object WorldSettings {
        @JvmField val GRAVITY: Vector2 = Vector2(0f, -9.8f)
        const val TIMESTEP: Float = 1 / 60f
        const val VELOCITY_ITER: Int = 6
        const val POSITION_ITER: Int = 2
    }

    val world: World
    var debugging: Boolean
    private val debugRenderer: PhysicsDebugger

    init {
        world = World(GRAVITY, true)
        debugRenderer = PhysicsDebugger(world)
        debugging = false
    }

    fun resize(width: Int, height: Int) = debugRenderer.resize(width, height)

    fun update() = world.step(TIMESTEP, VELOCITY_ITER, POSITION_ITER)

    fun render() {
        if (debugging) {
            debugRenderer.render()
        }
    }
}
