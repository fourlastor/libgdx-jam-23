package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.input.controls.Controls;

/**
 * Request to create a Player.
 */
public class PlayerRequestComponent implements Component {

    public final String name;
    public final Controls controls;
    public final boolean flipped;

    public PlayerRequestComponent(String name, Controls controls, boolean flipped) {
        this.name = name;
        this.controls = controls;
        this.flipped = flipped;
    }
}
