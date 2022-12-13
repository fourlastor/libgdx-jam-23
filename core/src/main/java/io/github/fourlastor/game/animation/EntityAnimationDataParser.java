package io.github.fourlastor.game.animation;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.fourlastor.game.animation.data.AnimatedValue;
import io.github.fourlastor.game.animation.data.AnimationData;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.animation.json.AnimatedSlot;
import io.github.fourlastor.game.animation.json.Animation;
import io.github.fourlastor.game.animation.json.EntityData;
import io.github.fourlastor.game.animation.json.KeyFrame;
import io.github.fourlastor.game.animation.json.Skin;
import io.github.fourlastor.game.animation.json.Skins;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityAnimationDataParser {

    private final Map<String, Integer> animationLengths = new HashMap<>();

    private final TextureAtlas atlas;

    @Inject
    public EntityAnimationDataParser(TextureAtlas atlas) {
        this.atlas = atlas;
        animationLengths.put("idle", 800);
        animationLengths.put("kick", 800);
    }

    public CharacterAnimationData parseCharacterData(EntityData data) {
        return new CharacterAnimationData(
                parseHitboxes(data.skins.get("hitbox")),
                parseAnimations(data.animations)
        );
    }

    private Map<String, AnimationData> parseAnimations(Map<String, Animation> animations) {
        HashMap<String, AnimationData> animationMap = new HashMap<>();
        for (Animation it : animations.values()) {
            animationMap.put(it.name, new AnimationData(
                    parseSprite(animationLengths.get(it.name), it),
                    parseHitboxesValues(it)
            ));
        }
        return animationMap;
    }

    private AnimatedValue<String> parseHitboxesValues(Animation animation) {
        AnimatedSlot hitbox = animation.slots.getOrDefault("hitbox", new AnimatedSlot(Collections.singletonList(
                new KeyFrame<>(0, "")
        )));
        return new AnimatedValue<>(
                hitbox.keyFrames,
                800
        );
    }

    private Map<String, Rectangle> parseHitboxes(Skins skins) {
        HashMap<String, Rectangle> hitboxes = new HashMap<>();
        for (Skin it : skins.skins.values()) {
            if (it instanceof Skin.BoundingBox) {
                float maxX = Float.MIN_VALUE;
                float maxY = Float.MIN_VALUE;
                float minX = Float.MAX_VALUE;
                float minY = Float.MAX_VALUE;
                float[] vertices = ((Skin.BoundingBox) it).vertices;
                for (int i = 0; i < vertices.length; i += 2) {
                    float x = vertices[i];
                    float y = vertices[i + 1];
                    maxX = Math.max(x, maxX);
                    minX = Math.min(x, minX);
                    maxY = Math.max(y, maxY);
                    minY = Math.min(y, minY);
                }
                float halfWidth = (maxX - minX) / 2f;
                float halfHeight = (maxY - minY) / 2f;
                float centerX = minX + halfWidth;
                float centerY = minY + halfHeight;
                hitboxes.put(it.name, new Rectangle(
                        centerX, centerY, halfWidth, halfHeight
                ));
            }
        }
        return hitboxes;
    }

    public AnimatedValue<TextureRegionDrawable> parseAnimation(EntityData entityData, String animationName, int duration) {
        Animation animation = entityData.animations.get(animationName);
        return parseSprite(duration, animation);
    }

    private AnimatedValue<TextureRegionDrawable> parseSprite(int duration, Animation animation) {
        return new AnimatedValue<>(
                parseKeyFrames("Character", animation.slots.get("Character").keyFrames),
                duration);
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
