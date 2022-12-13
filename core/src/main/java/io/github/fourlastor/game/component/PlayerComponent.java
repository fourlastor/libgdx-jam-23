package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Kicking;
import io.github.fourlastor.game.level.input.state.OnGround;

/** Bag containing the player state machine, and the possible states it can get into. */
public class PlayerComponent implements Component {
    public final InputStateMachine stateMachine;
    public final OnGround onGround;
    public final Kicking kicking;

    public PlayerComponent(
            InputStateMachine stateMachine,
            OnGround onGround, Kicking kicking) {
        this.stateMachine = stateMachine;
        this.onGround = onGround;
        this.kicking = kicking;
    }
}
