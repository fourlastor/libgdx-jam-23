package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Collections;
import java.util.Map;

/**
 * Actual body already running in Box2D
 */
public class BodyComponent implements Component {

    public Body body;
    public final Map<String, Fixture> hitboxes;

    public BodyComponent(Body body) {
        this(body, Collections.emptyMap());
    }

    public BodyComponent(Body body, Map<String, Fixture> hitboxes) {
        this.body = body;
        this.hitboxes = hitboxes;
    }
}
