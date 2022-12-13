package io.github.fourlastor.game.animation.data;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationData {
    public final AnimatedValue<TextureRegionDrawable> sprite;
    public final AnimatedValue<String> hitbox;

    public AnimationData(AnimatedValue<TextureRegionDrawable> sprite, AnimatedValue<String> hitbox) {
        this.sprite = sprite;
        this.hitbox = hitbox;
    }
}
