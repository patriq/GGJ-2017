package com.theend.game.core.data.song;

public class Beat {

    private int beatPosition;
    private int beatDuration;

    private int chord;

    public Beat(int beatPosition, int beatDuration, int chord) {
        this.beatPosition = beatPosition;
        this.beatDuration = beatDuration;
        this.chord = chord;
    }

    public int getBeatDuration() {
        return beatDuration;
    }

    public int getBeatPosition() {
        return beatPosition;
    }

    public int getChord() {
        return chord;
    }

    @Override
    public String toString() {
        return "Beat{" +
                "beatDuration=" + beatDuration +
                ", beatPosition=" + beatPosition +
                ", chord=" + chord +
                '}';
    }
}
