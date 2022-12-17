package io.github.fourlastor.game.animation.json;

import java.util.Map;

public class Animation {
    public final String name;
    public final Map<String, AnimatedSlot> slots;

    public Animation(String name, Map<String, AnimatedSlot> slots) {
        this.name = name;
        this.slots = slots;
    }

}
