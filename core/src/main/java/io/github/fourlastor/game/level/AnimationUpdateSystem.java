package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.fourlastor.game.animation.data.AnimatedValue;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.json.KeyFrame;
import io.github.fourlastor.game.component.AnimationChangeComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.PlayerComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AnimationUpdateSystem extends EntitySystem {

    private static final Family FAMILY = Family.all(AnimationChangeComponent.class).get();


    private final TextureAtlas atlas;
    private final ComponentMapper<PlayerComponent> players;
    private final ComponentMapper<AnimationImageComponent> animations;

    @Inject
    public AnimationUpdateSystem(
            TextureAtlas atlas,
            ComponentMapper<PlayerComponent> players,
            ComponentMapper<AnimationImageComponent> animations) {
        super();
        this.atlas = atlas;
        this.players = players;
        this.animations = animations;
    }


    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY, onAnimationChange);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(onAnimationChange);
        super.removedFromEngine(engine);
    }

    private final EntityListener onAnimationChange = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
            AnimationData animationData = entity.remove(AnimationChangeComponent.class).animationData;
            String name = players.get(entity).fighter.charName;
            animations.get(entity).image.setAnimatedValue(parseKeyFrames("animations/" + name + "/library", animationData.sprite), animationData.duration);
        }

        @Override
        public void entityRemoved(Entity entity) {

        }
    };


    private AnimatedValue<TextureRegionDrawable> parseKeyFrames(String path, List<KeyFrame<String>> json) {
        List<KeyFrame<TextureRegionDrawable>> keyFrames = new ArrayList<>(json.size());
        for (KeyFrame<String> it : json) {
            keyFrames.add(new KeyFrame<>(
                    it.start,
                    new TextureRegionDrawable(atlas.findRegion(path + "/" + it.value))
            ));
        }
        return new AnimatedValue<>(keyFrames);
    }

}
