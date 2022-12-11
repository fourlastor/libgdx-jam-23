package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.ChargeJump;
import io.github.fourlastor.game.level.input.state.Falling;
import io.github.fourlastor.game.level.input.state.Jumping;
import io.github.fourlastor.game.level.input.state.OnGround;

/** Bag containing the player state machine, and the possible states it can get into. */
public class PlayerComponent implements Component {
    public final InputStateMachine stateMachine;
    public final OnGround onGround;
    public float charge = 0f;

    public PlayerComponent(
            InputStateMachine stateMachine,
            OnGround onGround) {
        this.stateMachine = stateMachine;
        this.onGround = onGround;
    }
}
