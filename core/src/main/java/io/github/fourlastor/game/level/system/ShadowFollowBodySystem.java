package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.ShadowComponent;

import javax.inject.Inject;

public class ShadowFollowBodySystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(BodyComponent.class, ShadowComponent.class).get();
    private final ComponentMapper<BodyComponent> bodies;
    private final ComponentMapper<ShadowComponent> shadows;

    @Inject
    public ShadowFollowBodySystem(ComponentMapper<BodyComponent> bodies, ComponentMapper<ShadowComponent> shadows) {
        super(FAMILY);
        this.bodies = bodies;
        this.shadows = shadows;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 center = bodies.get(entity).body.getPosition();
        Body actor = shadows.get(entity).body;
        actor.setTransform(center, 0f);
    }
}
