package com.theend.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.theend.game.core.data.song.Beat;
import com.theend.game.core.data.song.Song;

import java.util.ArrayList;

public class MusicCreator extends ApplicationAdapter {

    private static final boolean LISTEN = true;

    private Song song;
    private int beat = 1;

    float r, g, b;

    @Override
    public void create() {
        song = new Song("bpm");
        if (LISTEN)
            song.addBeatListener((e) -> randomColor());
        Gdx.input.setInputProcessor(new InputListener());
        song.play();
    }

    private void randomColor(){
        r = (float) (Math.random() * 1);
        g = (float) (Math.random() * 1);
        b = (float) (Math.random() * 1);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(r, g, b, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        song.update(Gdx.graphics.getDeltaTime());
    }

    class InputListener implements InputProcessor {

        private ArrayList<Beat> beats = new ArrayList<>();

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.Q) {
                beats.forEach((it) -> System.out.printf("%d/%d-%d\n", it.getBeatPosition(), it.getBeatDuration(), it.getChord()));
            }
            int chord = 0;
            if (keycode == Input.Keys.Z) chord = 1;
            if (keycode == Input.Keys.X) chord = 2;
            if (keycode == Input.Keys.C) chord = 3;
            if (keycode == Input.Keys.V) chord = 4;
            beats.add(new Beat((int) ((song.getPosition() - 0.2f) * 1000), 0, chord));
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
