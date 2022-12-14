package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Collections;
import java.util.List;

/**
 * Actual body already running in Box2D
 */
public class BodyComponent implements Component {

    public Body body;
    public final List<Box> hitboxes;

    public BodyComponent(Body body) {
        this(body, Collections.emptyList());
    }

    public BodyComponent(Body body, List<Box> hitboxes) {
        this.body = body;
        this.hitboxes = hitboxes;
    }

    public static class Box {
        public final String name;
        public final Fixture feature;

        public Box(String name, Fixture feature) {
            this.name = name;
            this.feature = feature;
        }
    }
}
