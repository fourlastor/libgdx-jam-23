package io.github.fourlastor.game.animation.json;

public class Bone {

    public final String parent;
    public final String name;
    public final float x;
    public final float y;
    public final int rotation;
    public final float scaleX;
    public final float scaleY;

    public Bone(String name, String parent, float x, float y, int rotation, float scaleX, float scaleY) {
        this.name = name;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public String toString() {
        return "Bone{" +
                "parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", rotation=" + rotation +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                '}';
    }
}
