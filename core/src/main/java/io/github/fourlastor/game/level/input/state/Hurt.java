package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.animation.AnimationImage;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.input.controls.Controls;

import java.util.Map;

public class Hurt extends InputState {

    private final AnimationData animation;

    private float totalTime;
    private float redTime;
    private boolean red;

    @AssistedInject
    public Hurt(
            @Assisted String name,
            @Assisted Controls controls,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            Map<String, CharacterAnimationData> animations, ComponentMapper<HpComponent> hps
    ) {
        super(players, bodies, images, hps, controls);
        this.animation = animations.get(name).animations.get("idle");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        totalTime = 0f;
        redTime = 0f;
        red = true;
        images.get(entity).image.setColor(Color.RED);
        float x = players.get(entity).player.flipped ? 2f : -2f;
        bodies.get(entity).body.setLinearVelocity(x, 0);
        HpComponent hp = hps.get(entity);
        hp.hp = Math.max(hp.hp - 10, 0);
        hp.hpChanged = true;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        redTime += delta();
        totalTime += delta();
        if (totalTime >= 0.3f) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.idle);
            return;
        }

        AnimationImage image = images.get(entity).image;
        if (redTime >= 0.16f) {
            redTime = 0;
            if (red) {
                image.setColor(Color.WHITE);
            } else {
                image.setColor(Color.RED);
            }
            red = !red;
        }

    }

    @Override
    public void exit(Entity entity) {
        super.exit(entity);
        images.get(entity).image.setColor(Color.WHITE);
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    @AssistedFactory
    public interface Factory {
        Hurt create(String name, Controls controls);
    }
}
