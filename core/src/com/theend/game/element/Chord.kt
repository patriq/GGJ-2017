package com.theend.game.element

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef
import com.theend.game.b2d.toB2DUnits
import com.theend.game.b2d.toPixels

/**
 * A [Chord] is composed by two supports (top and bottom) and a rope.
 */

class Chord(val world: World, val renderer: ShapeRenderer, topSupportPosition: Vector2, val color: Color) {

    companion object {
        @JvmField val CHORD_COLOR: Color = Color.BLACK
        private const val CHORD_LENGTH: Float = 900f
        private const val MAX_CHORD_SIZE: Float = 4f
        private const val MIDDLE_SEGMENT_WIDTH: Float = 4f
        private const val MIDDLE_SEGMENT_HEIGHT: Float = 130f
        private const val CHORD_THICKNESS: Float = 2f
        private const val OUT_OF_BOUNDS_GAP: Float = -100f
        const val SUPPORT_SIZE: Float = 30f
    }

    private val topSupport: Body
    private val botSupport: Body
    private val ropes: List<Joint>
    var middleSegment: Body

    var fallingNotes: MutableList<Note>

    init {
        topSupport = createSupport(topSupportPosition, SUPPORT_SIZE, SUPPORT_SIZE)
        val botSupportPosition: Vector2 = Vector2(topSupportPosition.x, topSupportPosition.y - CHORD_LENGTH)
        botSupport = createSupport(botSupportPosition, SUPPORT_SIZE, SUPPORT_SIZE)
        val middlePosition: Vector2 = topSupportPosition.cpy().sub(10f, CHORD_LENGTH / 2f + 30f / 2f)
        middleSegment = createMiddleSegment(middlePosition, MIDDLE_SEGMENT_WIDTH, MIDDLE_SEGMENT_HEIGHT)
        ropes = createRopes()
        fallingNotes = mutableListOf()
    }

    /**
     * Creates a [PolygonShape.setAsBox].
     *
     * @param position  position of the support, in pixels.
     * @param width     width of the support, in pixels.
     * @param height    height of the support, in pixels.
     */
    private fun createBoxBody(position: Vector2, width: Float, height: Float, type: BodyDef.BodyType, fixtureDef: FixtureDef): Body {
        val bodyDef: BodyDef = BodyDef().apply {
            this.position.set(position).toB2DUnits()
            this.type = type
        }
        val shape: Shape = PolygonShape().apply {
            val halfWidth: Float = (width / 2f).toB2DUnits()
            val halfHeight: Float = (height / 2f).toB2DUnits()
            this.setAsBox(halfWidth, halfHeight)
        }
        fixtureDef.shape = shape
        return world.createBody(bodyDef).apply { this.createFixture(fixtureDef) }
    }

    private fun createSupport(position: Vector2, width: Float, height: Float): Body {
        val fixtureDef: FixtureDef = FixtureDef().apply { this.density = 1.0f }
        return createBoxBody(position, width, height, BodyDef.BodyType.StaticBody, fixtureDef)
    }

    private fun createMiddleSegment(position: Vector2, width: Float, height: Float): Body {
        val fixtureDef: FixtureDef = FixtureDef().apply { this.density = 0.075f }
        return createBoxBody(position, width, height, BodyDef.BodyType.DynamicBody, fixtureDef)
    }

    /**
     * Creates two rope joints and connects them to the two supports and the middle segment.
     */
    private fun createRopes(): List<Joint> {
        val topRopeDef: RopeJointDef = RopeJointDef().apply {
            this.bodyA = topSupport
            this.bodyB = middleSegment
            this.localAnchorA.set(0f, -0.2f)
            this.localAnchorB.set(0f, 0.2f)
            this.maxLength = MAX_CHORD_SIZE
        }
        val botRopeDef: RopeJointDef = RopeJointDef().apply {
            this.bodyA = middleSegment
            this.bodyB = botSupport
            this.localAnchorA.set(0f, -0.2f)
            this.localAnchorB.set(0f, 0.2f)
            this.maxLength = MAX_CHORD_SIZE
        }
        return listOf(world.createJoint(topRopeDef), world.createJoint(botRopeDef))
    }

    /**
     * Renders all the segments of a rope [Joint].
     * Adapted from [Box2DDebugRenderer].
     */
    private fun Joint.renderRopeJoint() {
        val bodyA: Body = this.bodyA
        val bodyB: Body = this.bodyB
        val xf1: Transform = bodyA.transform
        val xf2: Transform = bodyB.transform
        val x1: Vector2 = xf1.position.toPixels()
        val x2: Vector2 = xf2.position.toPixels()
        val p1: Vector2 = this.anchorA.toPixels()
        val p2: Vector2 = this.anchorB.toPixels()
        drawSegment(x1, p1)
        drawSegment(p1, p2)
        drawSegment(x2, p2)
    }

    /**
     * Draws a line segment that starts at point x1 and ends at point x2.
     *
     * @param x1    line-segment beggining; a [Vector2] in pixels.
     * @param x2    line-segment end; a [Vector2] in pixels.
     */
    private fun drawSegment(x1: Vector2, x2: Vector2) {
        renderer.color = CHORD_COLOR
        renderer.rectLine(x1.x, x1.y, x2.x, x2.y, CHORD_THICKNESS)
    }

    private fun Note.outOfScreenBounds(): Boolean {
        return this.position.y <= OUT_OF_BOUNDS_GAP
    }

    fun update() {
        val markedToDelete: MutableList<Note> = mutableListOf()
        fallingNotes.forEach {
            val noteX: Float = middleSegment.position.toPixels().x - (it.width / 2f)
            it.keepUpPosition(Vector2(noteX, it.position.y))
            it.update()
            if (it.scale <= Note.IMPLOSION_SCALE_AMOUNT || it.outOfScreenBounds()) {
                markedToDelete.add(it)
            }
        }
        markedToDelete.forEach { fallingNotes.remove(it) }
    }

    /**
     * Everything renderer by the [ShapeRenderer] should be inside a
     * batch.begin - batch.end lock.
     */
    private fun renderChords(batch: Batch) {
        batch.end()
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        ropes.forEach { it.renderRopeJoint() }
        renderer.end()
        batch.begin()
    }

    fun render(batch: Batch) {
        renderChords(batch)
        fallingNotes.forEach { it.render(batch) }
    }

    fun dispose() = fallingNotes.forEach(Note::dispose)
}
