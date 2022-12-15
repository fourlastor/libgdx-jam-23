package io.github.fourlastor.game.level.input.controls;

public interface Control {

    boolean matches(int key);

    boolean pressed();

}
