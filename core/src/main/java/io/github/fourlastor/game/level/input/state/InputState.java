package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import io.github.fourlastor.game.animation.data.AnimatedValue;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.HurtData;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.input.controls.Controls;
import io.github.fourlastor.game.level.physics.Bits;

import java.util.List;

public abstract class InputState implements State<Entity> {

    protected final ComponentMapper<PlayerComponent> players;
    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<AnimationImageComponent> images;
    protected final ComponentMapper<HpComponent> hps;
    protected final Controls controls;

    private int playHead;
    private int lastIndex;

    public InputState(
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<AnimationImageComponent> images,
            ComponentMapper<HpComponent> hps,
            Controls controls) {
        this.players = players;
        this.bodies = bodies;
        this.images = images;
        this.hps = hps;
        this.controls = controls;
    }

    protected abstract AnimationData animation();

    protected final float delta() {
        return Gdx.graphics.getDeltaTime();
    }

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
            List<BodyComponent.Box> hitboxes = bodies.get(entity).hitboxes;
            for (int i = 0; i < hitboxes.size(); i++) {
                BodyComponent.Box box = hitboxes.get(i);
                Filter oldFilter = box.feature.getFilterData();
                Filter filter = new Filter();
                copyFilter(filter, oldFilter);
                filter.maskBits = box.name.equals(name)
                        ? Bits.Mask.HITBOX.bits
                        : Bits.Mask.DISABLED.bits;
                box.feature.setFilterData(filter);
            }
        }

        HpComponent hpComponent = hps.get(entity);
        if (hpComponent.hpChanged) {
            hpComponent.hpChanged = false;
            hpComponent.bar.clearActions();
            hpComponent.bar.addAction(Actions.scaleTo(
                    (float) hpComponent.hp / hpComponent.maxHp,
                    1f,
                    0.5f,
                    Interpolation.bounce
            ));
        }
    }

    /**
     * Filter#set doesn't exist in GWT.
     */
    public void copyFilter(Filter newFilter, Filter filter) {
        newFilter.categoryBits = filter.categoryBits;
        newFilter.maskBits = filter.maskBits;
        newFilter.groupIndex = filter.groupIndex;
    }

    @Override
    public void exit(Entity entity) {
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        Object extraInfo = telegram.extraInfo;
        if (!(extraInfo instanceof HurtData)) {
            return false;
        }
        HurtData data = (HurtData) extraInfo;
        if (telegram.message == Message.PLAYER_HIT.ordinal() && data.entity == entity) {
            PlayerComponent player = players.get(entity);
            player.stateMachine.changeState(player.hurt.withDamage(data.damage));
            return true;
        }
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
