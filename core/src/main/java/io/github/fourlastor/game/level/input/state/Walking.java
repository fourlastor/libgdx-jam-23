package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.input.controls.Controls;

import java.util.Map;

public class Walking extends InputState {

    private static final float VELOCITY = 4f;
    private final AnimationData animation;
    private final Camera camera;

    @AssistedInject
    public Walking(
            @Assisted String name,
            @Assisted Controls controls,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            ComponentMapper<HpComponent> hps,
            Map<String, CharacterAnimationData> animations,
            Camera camera) {
        super(players, bodies, images, hps, controls);
        this.camera = camera;
        this.animation = animations.get(name).animations.get("walking");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    private final Vector2 velocity = Vector2.Zero.cpy();

    @Override
    public boolean keyDown(Entity entity, int keycode) {
        if (controls.attack().matches(keycode)) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.attacking);
            return true;
        }
        return super.keyDown(entity, keycode);
    }

    @Override
    public boolean keyUp(Entity entity, int keycode) {
        boolean goingLeft = controls.left().matches(keycode) && velocity.x < 0;
        boolean goingRight = controls.right().matches(keycode) && velocity.x > 0;
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
        boolean goingLeft = controls.left().pressed();
        boolean goingRight = controls.right().pressed();
        if (goingLeft || goingRight) {
            velocity.x = goingLeft ? -VELOCITY : VELOCITY;
        }
        updateBodyVelocity(entity);
    }

    private void updateBodyVelocity(Entity entity) {
        Body body = bodies.get(entity).body;
        boolean goingLeft = velocity.x < 0;
        boolean atLeftLimit = body.getPosition().x - 1 <= camera.position.x - camera.viewportWidth / 2f;
        boolean atRightLimit = body.getPosition().x + 1 >= camera.position.x + camera.viewportWidth / 2f;
        if ((goingLeft && atLeftLimit) || (!goingLeft && atRightLimit)) {
            body.setLinearVelocity(Vector2.Zero);
        } else {
            body.setLinearVelocity(velocity);
        }
    }

    @AssistedFactory
    public interface Factory {
        Walking create(String name, Controls controls);
    }
}
