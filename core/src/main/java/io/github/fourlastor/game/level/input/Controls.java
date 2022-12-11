package io.github.fourlastor.game.level.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;

public enum Controls {

    LEFT(Input.Keys.LEFT), RIGHT(Input.Keys.RIGHT);

    private final IntArray keys;

    Controls(int... keys) {
        this.keys = new IntArray(keys);
    }

    public boolean matches(int key) {
        return keys.contains(key);
    }

}
