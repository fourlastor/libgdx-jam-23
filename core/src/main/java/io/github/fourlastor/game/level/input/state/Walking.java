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
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.input.controls.Controls;

import java.util.Map;

public class Walking extends OnGround {

    private final AnimationData animation;

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
        super(players, bodies, images, hps, controls, camera);
        this.animation = animations.get(name).animations.get("walking");
    }

    @Override
    protected AnimationData animation() {
        return animation;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        if (!isMoving()) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.idle);
        }
    }

    @AssistedFactory
    public interface Factory {
        Walking create(String name, Controls controls);
    }
}
