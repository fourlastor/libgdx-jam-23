package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Group;
import io.github.fourlastor.game.animation.AnimationImage;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.ActorComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerRequestComponent;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.blueprint.definitions.Platform;
import io.github.fourlastor.game.level.physics.Bits;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private final WorldConfig config;
    private final Map<String, CharacterAnimationData> animations;

    @Inject
    public EntitiesFactory(WorldConfig config, Map<String, CharacterAnimationData> animations) {
        this.config = config;
        this.animations = animations;
    }

    public Entity player(String name) {
        Entity entity = new Entity();
        AnimationImage image = new AnimationImage();
        float scale = config.scale;
        image.setScale(scale);
        entity.add(new AnimationImageComponent(image));
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(4.5f, 1.5f));
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.4f, 0.8f);
            FixtureDef def = new FixtureDef();
            def.shape = shape;
            def.filter.categoryBits = Bits.Category.BODY.bits;
            def.filter.maskBits = Bits.Mask.BODY.bits;
            def.restitution = 0.15f;
            body.createFixture(def).setUserData(UserData.PLAYER);
            def = new FixtureDef();
            def.shape = shape;
            def.filter.categoryBits = Bits.Category.HITBOX.bits;
            def.filter.maskBits = Bits.Mask.HITBOX.bits;
            def.isSensor = true;
            CharacterAnimationData animationData = animations.get(name);
            List<BodyComponent.Box> hitboxes = new ArrayList<>(animationData.hitboxes.size());
            for (Map.Entry<String, Rectangle> value : animationData.hitboxes.entrySet()) {
                Rectangle rectangle = value.getValue();
                shape.setAsBox(rectangle.width * scale, rectangle.height * scale, new Vector2(rectangle.x, rectangle.y).scl(scale), 0f);
                Fixture fixture = body.createFixture(def);
                hitboxes.add(new BodyComponent.Box(value.getKey(), fixture));
            }
            shape.dispose();
            return new BodyComponent(body, hitboxes);
        }));
        image.setPosition(-0.5f, -0.5f);
        Group group = new Group();
        group.addActor(image);

        entity.add(new ActorComponent(image, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequestComponent(name));
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
            return new BodyComponent(body);
        });
    }
}
