package io.github.fourlastor.game.animation.json;

public class KeyFrame<T> {
    public int start;
    public final T value;

    public KeyFrame(int start, T value) {
        this.start = start;
        this.value = value;
    }
}
