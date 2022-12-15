package io.github.fourlastor.game.level.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;

public enum Controls {

    LEFT(Input.Keys.LEFT, Input.Keys.W), RIGHT(Input.Keys.RIGHT, Input.Keys.S), ATTACK(Input.Keys.SPACE);

    private final IntArray keys;

    Controls(int... keys) {
        this.keys = new IntArray(keys);
    }

    public boolean matches(int key) {
        return keys.contains(key);
    }

    public boolean pressed() {
        for (int i = 0; i < keys.size; i++) {
            if (Gdx.input.isKeyPressed(keys.get(i))) {
                return true;
            }
        }
        return false;
    }

}
