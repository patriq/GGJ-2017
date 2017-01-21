package com.theend.game.b2d

import com.badlogic.gdx.math.Vector2

internal val PPM: Float = 100f

internal fun Float.toB2DUnits(): Float {
    return this / PPM
}

internal fun Vector2.toB2DUnits(): Vector2 {
    return this.scl(1 / com.theend.game.b2d.PPM)
}

internal fun Float.toPixels(): Float {
    return this * PPM
}

internal fun Vector2.toPixels(): Vector2 {
    return this.scl(com.theend.game.b2d.PPM)
}
