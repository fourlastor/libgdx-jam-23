package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.InputComponent;
import io.github.fourlastor.game.component.PlayerComponent;

import java.util.Map;

public class Idle extends OnGround {

    private final AnimationData animation;

    @AssistedInject
    public Idle(
            @Assisted String name,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            ComponentMapper<InputComponent> inputs,
            Map<String, CharacterAnimationData> animations,
            ComponentMapper<HpComponent> hps,
            Camera camera
    ) {
        super(players, bodies, images, hps, inputs, camera);
        this.animation = animations.get(name).animations.get("idle");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }


    @Override
    public void update(Entity entity) {
        super.update(entity);
        if (isMoving()) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.walking);
        }
    }

    @AssistedFactory
    public interface Factory {
        Idle create(String name);
    }
}
