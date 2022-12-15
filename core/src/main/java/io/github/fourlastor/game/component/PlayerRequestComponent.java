package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;

/**
 * Request to create a Player.
 */
public class PlayerRequestComponent implements Component {

    public final String name;

    public PlayerRequestComponent(String name) {
        this.name = name;
    }
}
