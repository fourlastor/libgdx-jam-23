package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.input.controls.Controls;

import java.util.Map;

public class Idle extends InputState {

    private final AnimationData animation;

    @AssistedInject
    public Idle(
            @Assisted String name,
            @Assisted Controls controls,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            Map<String, CharacterAnimationData> animations
    ) {
        super(players, bodies, images, controls);
        this.animation = animations.get(name).animations.get("idle");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    @Override
    public boolean keyDown(Entity entity, int keycode) {
        boolean goingLeft = controls.left().matches(keycode);
        boolean goingRight = controls.right().matches(keycode);
        if (goingLeft || goingRight) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.walking);
            return true;
        }
        if (controls.attack().matches(keycode)) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.attacking);
            return true;
        }
        return super.keyDown(entity, keycode);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        bodies.get(entity).body.setLinearVelocity(Vector2.Zero);
    }

    @AssistedFactory
    public interface Factory {
        Idle create(String name, Controls controls);
    }
}
