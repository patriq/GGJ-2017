package com.theend.game.element

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.theend.game.core.data.song.Beat
import com.theend.game.res.ResourceHandler

class Note(var color: Color, val position: Vector2, var width: Float, var height: Float, var beat: Beat) {

    companion object {
        const val IMPLOSION_SCALE_AMOUNT = 0.05f
        private const val PARTICLE_SCALE: Float = 10f
        private const val EFFECTS_PATH: String = "effects"
        private const val PARTICLES_PATH: String = "$EFFECTS_PATH/explosion.p"
    }

    private val region: TextureRegion
    private var imploding: Boolean
    var scale: Float

    var particles: ParticleEffect

    init {
        region = ResourceHandler.getTexture("circle")
        scale = 1f
        imploding = false
        particles = ParticleEffect().apply {
            this.load(Gdx.files.internal(PARTICLES_PATH), Gdx.files.internal(EFFECTS_PATH))
            val colorToArray: FloatArray = floatArrayOf(this@Note.color.r, this@Note.color.g, this@Note.color.b)
            this.emitters[0].tint.colors = colorToArray
            this.emitters[0].scale.setHigh(PARTICLE_SCALE)
        }
    }

    /**
     * Adjust this note's position.
     *
     * @param position  new position for the note's texture.
     */
    fun keepUpPosition(position: Vector2) {
        this.position.set(position)
    }

    fun startImploding() {
        this.imploding = true
        this.particles.start()
    }

    fun update() {
        particles.setPosition(this.position.x, this.position.y)
        if (imploding) {
            particles.setPosition(this.position.x, this.position.y)
            if (scale >= IMPLOSION_SCALE_AMOUNT) {
                scale -= IMPLOSION_SCALE_AMOUNT
            } else {
                imploding = false
            }
        }
    }

    fun render(batch: Batch) {
        batch.color = color
        batch.draw(region, position.x, position.y, 0f, 0f, width, height, scale, scale, 1f)
        /* Draw the particles, if needed they have already been started. */
        particles.draw(batch, Gdx.graphics.deltaTime)
    }

    fun dispose() {
        region.texture.dispose()
        particles.dispose()
    }
}
