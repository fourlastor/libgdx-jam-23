package io.github.fourlastor.game.animation.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import javax.inject.Inject;
import java.util.Arrays;

public class AnimatedImage extends Image implements Animated {

    private final TextureAtlas atlas;

    private float playTime = 0f;
    private int animationLength = 800;
    private int lastIndex = -1;

    private AnimatedValue<TextureRegionDrawable> animatedValue;

    @Inject
    public AnimatedImage(TextureAtlas atlas) {
        this.atlas = atlas;

        setWidth(5f);
        setHeight(5f);
        animatedValue = new AnimatedValue<>(
                Arrays.asList(
                        frame(0),
                        frame(1),
                        frame(2),
                        frame(3),
                        frame(4),
                        frame(5),
                        frame(6),
                        frame(7)
                )
        );
    }

    private AnimatedValue.KeyFrame<TextureRegionDrawable> frame(int frameNo) {
        return new AnimatedValue.KeyFrame<>(
                frameNo * 100,
                new TextureRegionDrawable(atlas.findRegion("Character/Idle " + frameNo))
        );
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
