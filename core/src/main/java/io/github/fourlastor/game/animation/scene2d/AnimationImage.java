package io.github.fourlastor.game.animation.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class AnimationImage extends Image implements Animated {

    private float playTime = 0f;
    private int lastIndex = -1;

    private AnimatedValue<TextureRegionDrawable> animatedValue = null;

    public AnimationImage() {
        setWidth(5f);
        setHeight(5f);
    }

    public void setAnimatedValue(AnimatedValue<TextureRegionDrawable> animatedValue) {
        this.animatedValue = animatedValue;
        playTime = 0f;
        lastIndex = -1;
    }

    @Override
    public void act(float delta) {
        AnimatedValue<TextureRegionDrawable> value = animatedValue;
        if (value == null) {
            return;
        }
        playTime += delta * 1000;
        playTime = playTime % value.duration;
        int index = value.findIndex((int) playTime);
        if (lastIndex != index) {
            lastIndex = index;
            setDrawable(value.get(index).value);
        }
        super.act(delta);
    }

    @Override
    public void enter(String name) {
    }
}
