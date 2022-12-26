package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
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
import io.github.fourlastor.game.level.input.controls.Command;

import java.util.Map;

public class Attacking extends CharacterState {

    private final AnimationData animation;

    @AssistedInject
    public Attacking(
            @Assisted String name,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            Map<String, CharacterAnimationData> animations,
            ComponentMapper<HpComponent> hps,
            ComponentMapper<InputComponent> inputs
    ) {
        super(players, bodies, images, hps, inputs);
        this.animation = animations.get(name).animations.get("attack_0");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    float timePassed = 0f;

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        timePassed = 0f;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        timePassed += Gdx.graphics.getDeltaTime();
        if (timePassed * 1000 >= animation.duration) {
            PlayerComponent player = players.get(entity);
            Command command = inputs.get(entity).command;
            if (command == Command.LEFT || command == Command.RIGHT) {
                player.stateMachine.changeState(player.walking);
            } else {
                player.stateMachine.changeState(player.idle);
            }
        }
    }


    @AssistedFactory
    public interface Factory {
        Attacking create(String name);
    }
}
