package com.theend.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.theend.game.core.data.song.Beat;
import com.theend.game.core.data.song.BeatEvent;
import com.theend.game.core.data.song.BeatListener;
import com.theend.game.core.data.song.Song;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MusicCreator extends ApplicationAdapter implements BeatListener {

    private static final boolean LISTEN = false;
    float r, g, b;
    private Song song;

    private List<Float> pressTimes = new LinkedList<>();

    @Override
    public void create() {
        song = new Song("sweet");
        if (LISTEN)
            song.addBeatListener(this);
        Gdx.input.setInputProcessor(new InputListener());
        song.play();
    }

    private void randomColor() {
        r = (float) (Math.random() * 1);
        g = (float) (Math.random() * 1);
        b = (float) (Math.random() * 1);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(r, g, b, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        song.update(Gdx.graphics.getDeltaTime());
        Iterator<Float> timeIterator = pressTimes.iterator();
        while (timeIterator.hasNext())
            if(song.getPosition() - timeIterator.next() > 500) timeIterator.remove();
    }

    @Override
    public void onBeat(BeatEvent event) {
        for (float time : pressTimes) {
            if (Math.abs(event.getMusicPosition() - time) < 300) {
                randomColor();
            }
        }
    }

    class InputListener implements InputProcessor {

        private ArrayList<Beat> beats = new ArrayList<>();

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.Q) {
                beats.forEach((it) -> System.out.printf("%d/%d-%d\n", it.getBeatPosition(), it.getBeatDuration(), it.getChord()));
            }
            int chord = -1;
            if (keycode == Input.Keys.Z) chord = 0;
            if (keycode == Input.Keys.X) chord = 1;
            if (keycode == Input.Keys.C) chord = 2;
            if (keycode == Input.Keys.V) chord = 3;
            if (chord >= 0)
                beats.add(new Beat((int) ((song.getPosition() - 0.2f*1000)), 0, chord));

            pressTimes.add(song.getPosition());
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
