package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.physics.box2d.Fixture;
import io.github.fourlastor.game.animation.data.AnimatedValue;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.physics.Bits;

import java.util.Map;

public abstract class InputState implements State<Entity> {

    protected final ComponentMapper<PlayerComponent> players;
    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<AnimationImageComponent> images;

    private int playHead;
    private int lastIndex;

    public InputState(
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images) {
        this.players = players;
        this.bodies = bodies;
        this.images = images;
    }

    protected abstract AnimationData animation();

    @Override
    public void enter(Entity entity) {
        images.get(entity).image.setAnimatedValue(animation().sprite, animation().duration);
        playHead = 0;
        lastIndex = -1;
    }

    @Override
    public void update(Entity entity) {
        playHead += Gdx.graphics.getDeltaTime() * 1000;

        AnimatedValue<String> hitbox = animation().hitbox;
        int index = hitbox.findIndex(playHead);
        if (index != lastIndex) {
            String name = hitbox.get(index).value;
            lastIndex = index;
            for (Map.Entry<String, Fixture> it : bodies.get(entity).hitboxes.entrySet()) {
                it.getValue().getFilterData().maskBits = it.getKey().equals(name)
                        ? Bits.Mask.HITBOX.bits
                        : Bits.Mask.DISABLED.bits;
            }
        }
    }

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }

    public boolean keyDown(Entity entity, int keycode) {
        return false;
    }

    public boolean keyUp(Entity entity, int keycode) {
        return false;
    }

    public boolean touchDown(Entity entity, int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Entity entity, int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
