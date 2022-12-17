package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.animation.AnimationImage;

public class AnimationImageComponent implements Component {

    public final AnimationImage image;

    public AnimationImageComponent(AnimationImage image) {
        this.image = image;
    }
}
