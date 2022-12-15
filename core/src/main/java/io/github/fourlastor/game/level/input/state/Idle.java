package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.PlayerAnimationsFactory;
import io.github.fourlastor.game.level.input.Controls;

import javax.inject.Inject;
import javax.inject.Named;

public class Idle extends InputState {

    private final AnimationData animation;

    @Inject
    public Idle(
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            @Named(PlayerAnimationsFactory.KARATENISSE) CharacterAnimationData animationData) {
        super(players, bodies, images);
        this.animation = animationData.animations.get("idle");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    @Override
    public boolean keyDown(Entity entity, int keycode) {
        boolean goingLeft = Controls.LEFT.matches(keycode);
        boolean goingRight = Controls.RIGHT.matches(keycode);
        if (goingLeft || goingRight) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.walking);
            return true;
        }
        if (Controls.ATTACK.matches(keycode)) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.attacking);
            return true;
        }
        return super.keyDown(entity, keycode);
    }
}
