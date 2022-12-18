package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.level.WorldConfig;

import javax.inject.Inject;

public class CameraMovementSystem extends EntitySystem {

    private final Camera camera;
    private final float minX;
    private final float maxX;
    private Body p1;
    private Body p2;

    @Inject
    public CameraMovementSystem(Camera camera, WorldConfig config) {
        this.camera = camera;
        minX = camera.viewportWidth / 2;
        maxX = 512 * config.scale - camera.viewportWidth / 2;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (p1 == null || p2 == null) {
            return;
        }
        Vector2 p1Position = p1.getPosition();
        Vector2 p2Position = p2.getPosition();
        float x = p1Position.x + p1Position.dst(p2Position) / 2;
        camera.position.x = MathUtils.clamp(x, minX, maxX);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Family.all(PlayerComponent.class, BodyComponent.class).get(), entityListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(entityListener);
        p1 = null;
        p2 = null;
        super.removedFromEngine(engine);
    }

    private final EntityListener entityListener = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
            PlayerComponent player = entity.getComponent(PlayerComponent.class);
            Body body = entity.getComponent(BodyComponent.class).body;
            if (player.player == Player.P1) {
                p1 = body;
            } else {
                p2 = body;
            }
        }

        @Override
        public void entityRemoved(Entity entity) {

        }
    };
}
