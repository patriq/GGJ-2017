package com.theend.game.res

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.theend.game.Game

object ResourceHandler {

    lateinit var atlas: TextureAtlas
    lateinit var fonts: MutableMap<String, BitmapFont>

    fun init(atlasName: String) {
        atlas = TextureAtlas(Gdx.files.internal(atlasName))
        fonts = mutableMapOf()
    }

    fun getTexture(textureName: String): TextureRegion {
        return atlas.findRegion(textureName)
    }

    fun createFont(fontPath: String, key: String, size: Int) {
        val gen: FreeTypeFontGenerator = FreeTypeFontGenerator(Gdx.files.internal(fontPath))
        val params: FreeTypeFontGenerator.FreeTypeFontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        val scale: Float = 1.0f * Gdx.graphics.width / Game.WORLD_WIDTH
        params.minFilter = Texture.TextureFilter.Linear
        params.magFilter = Texture.TextureFilter.Linear
        params.size = (size * scale).toInt()
        val font: BitmapFont = gen.generateFont(params)
        font.data.scale(1.0f / scale)
        fonts.put(key, font)
        gen.dispose()
    }
}