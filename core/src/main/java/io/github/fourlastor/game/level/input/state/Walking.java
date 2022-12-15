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
import io.github.fourlastor.game.level.input.Controls;

import java.util.Map;

public class Walking extends InputState {

    private static final float VELOCITY = 4f;
    private final AnimationData animation;

    @AssistedInject
    public Walking(
            @Assisted String name,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            Map<String, CharacterAnimationData> animations) {
        super(players, bodies, images);
        this.animation = animations.get(name).animations.get("walking");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    private final Vector2 velocity = Vector2.Zero.cpy();

    @Override
    public boolean keyDown(Entity entity, int keycode) {
        if (Controls.ATTACK.matches(keycode)) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.attacking);
            return true;
        }
        return super.keyDown(entity, keycode);
    }

    @Override
    public boolean keyUp(Entity entity, int keycode) {
        boolean goingLeft = Controls.LEFT.matches(keycode) && velocity.x < 0;
        boolean goingRight = Controls.RIGHT.matches(keycode) && velocity.x > 0;
        if (goingLeft || goingRight) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.idle);
            return true;
        }

        return super.keyUp(entity, keycode);
    }

    @Override
    public void exit(Entity entity) {
        velocity.set(Vector2.Zero);
        updateBodyVelocity(entity);
        super.exit(entity);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        boolean goingLeft = Controls.LEFT.pressed();
        boolean goingRight = Controls.RIGHT.pressed();
        if (goingLeft || goingRight) {
            velocity.x = goingLeft ? -VELOCITY : VELOCITY;
        }
        updateBodyVelocity(entity);
    }

    private void updateBodyVelocity(Entity entity) {
        bodies.get(entity).body.setLinearVelocity(velocity);
    }

    @AssistedFactory
    public interface Factory {
        Walking create(String name);
    }
}
