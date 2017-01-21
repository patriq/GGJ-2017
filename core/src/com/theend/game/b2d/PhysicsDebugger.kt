package com.theend.game.b2d

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.theend.game.Game

class PhysicsDebugger(val world: World) {

    private val debugRenderer: Box2DDebugRenderer
    private val b2dCamera: Camera
    private val b2dViewport: Viewport

    init {
        debugRenderer = Box2DDebugRenderer()
        val vpWidth: Float = Game.WORLD_WIDTH.toFloat() / PPM
        val vpHeight: Float = Game.WORLD_HEIGHT.toFloat() / PPM
        b2dCamera = OrthographicCamera().apply {
            setToOrtho(false, vpWidth, vpHeight)
        }
        b2dViewport = ExtendViewport(vpWidth, vpHeight, b2dCamera)
    }

    fun resize(width: Int, height: Int) = b2dViewport.update(width, height)

    fun render() = debugRenderer.render(world, b2dCamera.combined)
}
