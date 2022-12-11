package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.component.AnimatedImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.PlayerAnimationsFactory;
import io.github.fourlastor.game.level.input.Controls;

import javax.inject.Inject;
import javax.inject.Named;

public class OnGround extends InputState {

    private static final float VELOCITY = 4f;
    private final Animation<TextureRegion> animation;

    @Inject
    public OnGround(
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimatedImageComponent> images,
            @Named(PlayerAnimationsFactory.WHITE_PIXEL) Animation<TextureRegion> animation) {
        super(players, bodies, images);
        this.animation = animation;
    }

    @Override
    protected Animation<TextureRegion> animation() {
        return animation;
    }

    private Vector2 velocity = Vector2.Zero.cpy();

    @Override
    public boolean keyDown(Entity entity, int keycode) {
        boolean goingLeft = Controls.LEFT.matches(keycode);
        boolean goingRight = Controls.RIGHT.matches(keycode);
        if (goingLeft || goingRight) {
            velocity.x = goingLeft ? -VELOCITY : VELOCITY;
            return true;
        }
        return super.keyDown(entity, keycode);
    }

    @Override
    public boolean keyUp(Entity entity, int keycode) {
        boolean goingLeft = Controls.LEFT.matches(keycode) && velocity.x < 0;
        boolean goingRight = Controls.RIGHT.matches(keycode) && velocity.x > 0;
        if (goingLeft || goingRight) {
            velocity.x = 0;
            return true;
        }

        return super.keyUp(entity, keycode);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        bodies.get(entity).body.setLinearVelocity(velocity);
    }
}
