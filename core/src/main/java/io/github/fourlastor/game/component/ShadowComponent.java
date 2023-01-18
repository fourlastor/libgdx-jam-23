package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class ShadowComponent implements Component {

    public Body body;

    public ShadowComponent(Body body) {
        this.body = body;
    }
}
