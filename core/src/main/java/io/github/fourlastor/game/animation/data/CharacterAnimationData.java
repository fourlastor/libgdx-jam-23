package io.github.fourlastor.game.animation.data;

import com.badlogic.gdx.math.Rectangle;

import java.util.Map;

public class CharacterAnimationData {
    public final Map<String, Rectangle> hitboxes;
    public final Map<String, AnimationData> animations;

    public CharacterAnimationData(Map<String, Rectangle> hitboxes, Map<String, AnimationData> animations) {
        this.hitboxes = hitboxes;
        this.animations = animations;
    }
}
