package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.Fighter;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.level.input.controls.Controls;

/**
 * Request to create a Player.
 */
public class PlayerRequestComponent implements Component {

    public final Fighter fighter;
    public final Controls controls;
    public final Player player;

    public PlayerRequestComponent(Fighter fighter, Controls controls, Player player) {
        this.fighter = fighter;
        this.controls = controls;
        this.player = player;
    }
}
