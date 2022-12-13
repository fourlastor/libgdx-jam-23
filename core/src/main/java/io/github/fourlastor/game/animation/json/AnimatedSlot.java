package io.github.fourlastor.game.animation.json;

import java.util.List;

public class AnimatedSlot {

    public final List<KeyFrame<String>> keyFrames;

    public AnimatedSlot(List<KeyFrame<String>> keyFrames) {
        this.keyFrames = keyFrames;
    }
}
