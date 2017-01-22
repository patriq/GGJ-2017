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
    private List<WarnListener> warnListeners;
    private List<Music.OnCompletionListener> completeListeners;
    private Music music;

    private float warnMilliseconds = 2000;
    private int nextWarnPosition;
    private Beat nextWarn;

    private float lastFrameMusicPosition;
    private Beat nextBeat;
    private int nextBeatPosition;

    private boolean moreWarns = true;
    private boolean moreBeats = true;

    public Song(String songName) {
        String songDirectory = SONGS_DIRECTORY + songName + "/";

        this.beats = new LinkedList<>();
        loadSongInfo(songDirectory + songName + SONG_EXTENSION);
        nextBeatPosition = nextWarnPosition = 0;
        nextWarn = nextBeat = this.beats.get(nextBeatPosition);

        this.music = Gdx.audio.newMusic(Gdx.files.internal(songDirectory + songName + AUDIO_EXTENSION));
        this.beatListeners = new LinkedList<>();
        this.warnListeners = new LinkedList<>();
        this.completeListeners = new LinkedList<>();
    }

    public void update(float delta) {
        if (music.isPlaying()) {
            float currentFrameMusicPosition = getPosition(); // Random delta / 2 :) maybe it helps
            while (moreWarns && nextWarn.getBeatPosition() > lastFrameMusicPosition + warnMilliseconds && nextWarn.getBeatPosition() <= currentFrameMusicPosition + warnMilliseconds) {
                BeatEvent event = new BeatEvent(nextWarn, currentFrameMusicPosition);
                for (WarnListener listener : warnListeners) listener.onWarn(event);
                if (nextWarnPosition + 1 >= this.beats.size()) {
                    moreWarns = false;
                    break;
                }
                nextWarn = this.beats.get(++nextWarnPosition);
            }
            while (moreBeats && nextBeat.getBeatPosition() > lastFrameMusicPosition && nextBeat.getBeatPosition() <= currentFrameMusicPosition) {
                BeatEvent event = new BeatEvent(nextBeat, currentFrameMusicPosition);
                for (BeatListener listener : beatListeners) listener.onBeat(event);
                if (nextBeatPosition + 1 >= this.beats.size()) {
                    moreBeats = false;
                    break;
                }
                nextBeat = this.beats.get(++nextBeatPosition);
            }
            lastFrameMusicPosition = currentFrameMusicPosition;
            if(!moreBeats){
                music.setVolume(music.getVolume()-music.getVolume()*delta);
                if(music.getVolume() < 0.05){
                    music.stop();
                    for (Music.OnCompletionListener listener : completeListeners) listener.onCompletion(music);
                }
            }
        }
    }

    public void play() {
        music.play();
    }

    public void pause() {
        music.pause();
    }

    public void fade() {
        moreBeats = false;
        moreWarns = false;
    }

    public float getPosition() {
        return music.getPosition() * 1000;
    }

    public void addWarnListener(WarnListener listener) {
        this.warnListeners.add(listener);
    }

    public void removeWarnListener(WarnListener listener) {
        this.warnListeners.remove(listener);
    }


    public void addBeatListener(BeatListener listener) {
        this.beatListeners.add(listener);
    }

    public void removeBeatListener(BeatListener listener) {
        this.beatListeners.remove(listener);
    }

    public void setOnCompletionListener(Music.OnCompletionListener listener) {
        this.completeListeners.add(listener);
        music.setOnCompletionListener(listener);
    }

    public List<Beat> getBeats() {
        return beats;
    }

    public float getWarnMilliseconds() {
        return warnMilliseconds;
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
