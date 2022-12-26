package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.Fighter;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.level.input.CharacterStateMachine;
import io.github.fourlastor.game.level.input.controls.Controls;
import io.github.fourlastor.game.level.input.state.Attacking;
import io.github.fourlastor.game.level.input.state.Hurt;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.game.level.input.state.Walking;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class PlayerComponent implements Component {

    public final Controls controls;
    public final CharacterStateMachine stateMachine;
    public final Idle idle;
    public final Walking walking;
    public final Attacking attacking;
    public final Hurt hurt;
    public final Player player;
    public final Fighter fighter;

    public PlayerComponent(
            Controls controls,
            CharacterStateMachine stateMachine,
            Idle idle,
            Walking walking,
            Attacking attacking,
            Hurt hurt,
            Player player,
            Fighter fighter) {
        this.controls = controls;
        this.stateMachine = stateMachine;
        this.idle = idle;
        this.walking = walking;
        this.attacking = attacking;
        this.hurt = hurt;
        this.player = player;
        this.fighter = fighter;
    }
}
