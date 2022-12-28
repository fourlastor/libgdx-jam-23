package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.animation.data.AnimationData;

public class AnimationChangeComponent implements Component {

    public final AnimationData animationData;

    public AnimationChangeComponent(AnimationData animationData) {
        this.animationData = animationData;
    }
}
