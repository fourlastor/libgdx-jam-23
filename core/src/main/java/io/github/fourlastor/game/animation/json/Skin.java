package io.github.fourlastor.game.animation.json;

public abstract class Skin {
    public final String name;

    protected Skin(String name) {
        this.name = name;
    }

    public static class BoundingBox extends Skin {
        public final int vertexCount;
        public final float[] vertices;

        public BoundingBox(String name, int vertexCount, float[] vertices) {
            super(name);
            this.vertexCount = vertexCount;
            this.vertices = vertices;
        }
    }

    public static class Image extends Skin {
        public Image(String name) {
            super(name);
        }
    }

    @Override
    public String toString() {
        return "Skin{" +
                "name='" + name + '\'' +
                '}';
    }
}
