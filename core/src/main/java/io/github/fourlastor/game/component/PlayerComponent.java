package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Attacking;
import io.github.fourlastor.game.level.input.state.Hurt;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.game.level.input.state.Walking;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class PlayerComponent implements Component {

    public final InputStateMachine stateMachine;
    public final Idle idle;
    public final Walking walking;
    public final Attacking attacking;
    public final Hurt hurt;
    public final boolean flipped;

    public PlayerComponent(
            InputStateMachine stateMachine,
            Idle idle,
            Walking walking,
            Attacking attacking,
            Hurt hurt,
            boolean flipped) {
        this.stateMachine = stateMachine;
        this.idle = idle;
        this.walking = walking;
        this.attacking = attacking;
        this.hurt = hurt;
        this.flipped = flipped;
    }
}
