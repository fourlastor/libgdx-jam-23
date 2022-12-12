package io.github.fourlastor.game.animation.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import javax.inject.Inject;

public class AnimationImage extends Image implements Animated {

    private float playTime = 0f;
    private int animationLength = 800;
    private int lastIndex = -1;

    private final AnimatedValue<TextureRegionDrawable> animatedValue;

    @Inject
    public AnimationImage(AnimatedValue<TextureRegionDrawable> animatedValue) {
        this.animatedValue = animatedValue;

        setWidth(5f);
        setHeight(5f);
    }


    @Override
    public void act(float delta) {
        if (animationLength == 0) {
            return;
        }
        playTime += delta * 1000;
        playTime = playTime % animationLength;
        int index = animatedValue.findIndex((int) playTime);
        if (lastIndex != index) {
            lastIndex = index;
            setDrawable(animatedValue.get(index).value);
        }
        super.act(delta);
    }

    @Override
    public void enter(String name) {
    }
}
