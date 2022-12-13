package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Group;
import io.github.fourlastor.game.animation.scene2d.AnimationImage;
import io.github.fourlastor.game.component.ActorComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.PlayerRequestComponent;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.blueprint.definitions.Platform;

import javax.inject.Inject;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private final WorldConfig config;

    @Inject
    public EntitiesFactory(WorldConfig config) {
        this.config = config;
    }

    public Entity player() {
        Entity entity = new Entity();
        AnimationImage image = new AnimationImage();
        image.setScale(config.scale);
        entity.add(new AnimationImageComponent(image));
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(4.5f, 1.5f));
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.4f, 0.8f);
            Fixture fixture = body.createFixture(shape, 0f);
//            fixture.setFriction(100f);
            fixture.setRestitution(0.15f);
            fixture.setUserData(UserData.PLAYER);
            shape.setAsBox(9f * config.scale, 4f * config.scale, new Vector2(15f * config.scale, 5f * config.scale), 0f);
            body.createFixture(shape, 0f).setSensor(true);
            shape.dispose();
            return body;
        }));
        image.setPosition(-0.5f, -0.5f);
        Group group = new Group();
        group.addActor(image);

        entity.add(new ActorComponent(image, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequestComponent());
        return entity;
    }

    public Entity base() {
        Entity entity = new Entity();
        Vector2 initialPosition = new Vector2(8f, 0.5f);
        entity.add(platformBuilder(initialPosition, Platform.Width.SIXTEEN));
        return entity;
    }

    private BodyBuilderComponent platformBuilder(Vector2 position, Platform.Width width) {
        return new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(position);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width.width / 2f, 0.25f);
            body.createFixture(shape, 0.0f).setUserData(UserData.PLATFORM);
            shape.dispose();
            return body;
        });
    }
}
