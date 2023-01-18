package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.InputComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.input.controls.Command;

public abstract class OnGround extends CharacterState {
    private static final float VELOCITY = 4f;
    private final Camera camera;
    private final Vector2 velocity = Vector2.Zero.cpy();

    public OnGround(ComponentMapper<PlayerComponent> players,
                    ComponentMapper<BodyComponent> bodies,
                    ComponentMapper<AnimationImageComponent> images,
                    ComponentMapper<HpComponent> hps,
                    ComponentMapper<InputComponent> inputs,
                    Camera camera) {
        super(players, bodies, images, hps, inputs);
        this.camera = camera;
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
        Command command = inputs.get(entity).command;
        if (command == Command.ATTACK) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.attacking);
            return;
        }
        boolean goingLeft = command == Command.LEFT;
        boolean goingRight = command == Command.RIGHT;
        if (goingLeft || goingRight) {
            velocity.x = goingLeft ? -VELOCITY : VELOCITY;
        } else {
            velocity.set(Vector2.Zero);
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

    protected final boolean isMoving() {
        return velocity.x != 0;
    }
}
