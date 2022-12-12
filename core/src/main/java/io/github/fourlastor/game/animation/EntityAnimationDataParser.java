package io.github.fourlastor.game.animation;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.fourlastor.game.animation.json.Animation;
import io.github.fourlastor.game.animation.json.EntityData;
import io.github.fourlastor.game.animation.json.KeyFrame;
import io.github.fourlastor.game.animation.scene2d.AnimatedValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EntityAnimationDataParser {

    private final TextureAtlas atlas;

    @Inject
    public EntityAnimationDataParser(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public AnimatedValue<TextureRegionDrawable> parseCharacter(EntityData entityData) {
        Animation idle = entityData.animations.get("idle");
        return new AnimatedValue<>(
                parseKeyFrames("Character", idle.slots.get("Character").keyFrames)
        );
    }

    private List<KeyFrame<TextureRegionDrawable>> parseKeyFrames(String path, List<KeyFrame<String>> json) {
        List<KeyFrame<TextureRegionDrawable>> keyFrames = new ArrayList<>(json.size());
        for (KeyFrame<String> it : json) {
            keyFrames.add(new KeyFrame<>(
                    it.start,
                    new TextureRegionDrawable(atlas.findRegion(path + "/" + it.value))
            ));
        }
        return keyFrames;
    }
}
