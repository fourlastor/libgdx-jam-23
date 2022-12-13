package io.github.fourlastor.game.animation.json;

public class Slot {
    public final String name;
    public final String bone;
    public final String attachment;

    public Slot(String name, String bone, String attachment) {
        this.name = name;
        this.bone = bone;
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "name='" + name + '\'' +
                ", bone='" + bone + '\'' +
                ", attachment='" + attachment + '\'' +
                '}';
    }
}
