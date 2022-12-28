package io.github.fourlastor.game.animation.data;

import io.github.fourlastor.game.animation.json.KeyFrame;

import java.util.List;

public class AnimationData {

    public final int duration;
    public final List<KeyFrame<String>> sprite;
    public final AnimatedValue<String> hitbox;

    public AnimationData(int duration, List<KeyFrame<String>> sprite, AnimatedValue<String> hitbox) {
        this.duration = duration;
        this.sprite = sprite;
        this.hitbox = hitbox;
    }
}
