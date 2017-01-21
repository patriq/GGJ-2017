package com.theend.game.core.data.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import java.util.LinkedList;
import java.util.List;

public class Song {

    private static String SONGS_DIRECTORY = "songs/";
    private static String SONG_EXTENSION = ".song";
    private static String AUDIO_EXTENSION = ".ogg";

    private List<Beat> beats;
    private List<BeatListener> beatListeners;
    private Music music;

    private float lastBeat;
    private float lastFrameMusicPosition;
    private Beat nextBeat;
    private int nextBeatPosition;

    public Song(String fileName) {
        this.beats = new LinkedList<>();
        loadSongInfo(SONGS_DIRECTORY + fileName + SONG_EXTENSION);
        nextBeatPosition = 0;
        nextBeat = this.beats.get(nextBeatPosition);

        this.music = Gdx.audio.newMusic(Gdx.files.internal(SONGS_DIRECTORY + fileName + AUDIO_EXTENSION));
        this.beatListeners = new LinkedList<>();
    }

    public void update(float delta) {
        if(music.isPlaying()){
            float currentFrameMusicPosition = (music.getPosition() - delta / 2) * 1000; // Random delta / 2 :) maybe it helps
            while (nextBeat.getBeatPosition() > lastFrameMusicPosition && nextBeat.getBeatPosition() <= currentFrameMusicPosition) {
                BeatEvent event = new BeatEvent(nextBeat, currentFrameMusicPosition);
                for (BeatListener listener : beatListeners) listener.onBeat(event);
                nextBeat = this.beats.get(++nextBeatPosition);
            }
            lastFrameMusicPosition = currentFrameMusicPosition;
        }
    }

    public void play() {
        music.play();
    }

    public void pause() {
        music.pause();
    }

    public void stop() {
        music.stop();
    }

    public float getPosition() {
        return music.getPosition();
    }

    public void addBeatListener(BeatListener listener) {
        this.beatListeners.add(listener);
    }

    public void removeBeatListener(BeatListener listener) {
        this.beatListeners.remove(listener);
    }

    public void setOnCompletionListener(Music.OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }

    private void loadSongInfo(String filePath) {
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String[] lines = fileHandle.readString().split(System.lineSeparator());
        for (String line : lines) {
            String[] lineComponents = line.trim().split("-");
            String[] beatInfo = lineComponents[0].split("/");
            int startPosition = Integer.parseInt(beatInfo[0]);
            int beatDuration = Integer.parseInt(beatInfo[1]);
            int chord = Integer.parseInt(lineComponents[1]);
            beats.add(new Beat(startPosition, beatDuration, chord));
        }
        System.out.println("Loaded beats: " + beats);
    }
}
