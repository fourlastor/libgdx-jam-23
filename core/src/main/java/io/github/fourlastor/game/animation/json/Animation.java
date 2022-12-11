package io.github.fourlastor.game.animation.json;

import java.util.List;
import java.util.Map;

public class Animation {

    public final List<Bone> bones;
    public final List<Slot> slots;

    public final Map<String, Skins> skins;

    public Animation(List<Bone> bones, List<Slot> slots, Map<String, Skins> skins) {
        this.bones = bones;
        this.slots = slots;
        this.skins = skins;
    }

    @Override
    public String toString() {
        return "Animation{" +
                "bones=" + bones +
                ", slots=" + slots +
                ", skins=" + skins +
                '}';
    }
}
