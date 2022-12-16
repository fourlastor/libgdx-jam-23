package io.github.fourlastor.game.animation.data;

import com.badlogic.gdx.math.Rectangle;

import java.util.Map;

public class CharacterAnimationData {

    public final int width;
    public final int height;
    public final Map<String, Rectangle> hitboxes;
    public final Map<String, Rectangle> hurtboxes;
    public final Map<String, AnimationData> animations;

    public CharacterAnimationData(int width, int height, Map<String, Rectangle> hitboxes,
                                  Map<String, Rectangle> hurtboxes,
                                  Map<String, AnimationData> animations) {
        this.width = width;
        this.height = height;
        this.hitboxes = hitboxes;
        this.hurtboxes = hurtboxes;
        this.animations = animations;
    }
}
