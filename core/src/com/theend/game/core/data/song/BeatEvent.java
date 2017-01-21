package com.theend.game.core.data.song;

public class BeatEvent {
    private Beat beat;
    private float musicPosition;

    public BeatEvent(Beat beat, float musicPosition) {
        this.beat = beat;
        this.musicPosition = musicPosition;
    }

    public Beat getBeat() {
        return beat;
    }

    public float getMusicPosition() {
        return musicPosition;
    }
}
