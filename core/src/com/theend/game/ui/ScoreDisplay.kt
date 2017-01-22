package com.theend.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.theend.game.res.ResourceHandler

class ScoreDisplay {

    private val font: BitmapFont?
    var score1: Int
    var score2: Int
    var misses: Int
    private val scoresRegion: TextureRegion

    init {
        font = BitmapFont(Gdx.files.internal("fonts/dual.fnt")).apply { this.color = Color.BLACK }
        score1 = 0
        score2 = 0
        misses = 0
        scoresRegion = ResourceHandler.getTexture("scores")
    }

    fun render(batch: Batch) {
        font?.draw(batch, "Player 1:\n\t$score1", 830f, 690f)
        font?.draw(batch, "Player 2:\n\t$score2", 830f, 600f)
        font?.draw(batch, "Misses:\n\t$misses", 830f, 510f)
    }
}