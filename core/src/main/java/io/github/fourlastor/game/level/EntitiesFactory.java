package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.fourlastor.game.MyGdxGame;
import io.github.fourlastor.game.animation.AnimationImage;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.ActorComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.PlayerRequestComponent;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.blueprint.definitions.Platform;
import io.github.fourlastor.game.level.input.controls.Controls;
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
    private final TextureAtlas atlas;

    @Inject
    public EntitiesFactory(WorldConfig config, Map<String, CharacterAnimationData> animations, TextureAtlas atlas) {
        this.config = config;
        this.animations = animations;
        this.atlas = atlas;
    }

    public Entity player(String name,
                         Controls controls,
                         Player player) {
        return player(name, controls, player, false);
    }

    public Entity player(String name,
                         Controls controls,
                         Player player,
                         boolean isImpostor) {
        CharacterAnimationData animationData = animations.get(name);
        Entity entity = new Entity();
        entity.add(new HpComponent());
        AnimationImage image = new AnimationImage();
        if (isImpostor) {
            image.setColor(MyGdxGame.IMPOSTOR_COLOR);
        }
        float scale = config.scale;
        int flippedFactor = player.flipped ? -1 : 1;
        image.setScale(flippedFactor * scale, scale);
        entity.add(new AnimationImageComponent(image));
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            float x = player.flipped ? 15f : 1f;
            float height = animationData.height * config.scale;

            bodyDef.position.set(new Vector2(x, height));
            bodyDef.allowSleep = false;
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.4f, 0.4f, new Vector2(0f, (-height / 2) + 0.4f), 0f);
            FixtureDef def = new FixtureDef();
            def.shape = shape;
            def.filter.categoryBits = Bits.Category.BODY.bits;
            def.filter.maskBits = Bits.Mask.BODY.bits;
            body.createFixture(def).setUserData(UserData.PLAYER);
            def = new FixtureDef();
            def.shape = shape;
            def.filter.categoryBits = Bits.Category.HITBOX.bits;
            def.filter.maskBits = Bits.Mask.HITBOX.bits;
            def.isSensor = true;
            List<BodyComponent.Box> hitboxes = new ArrayList<>(animationData.hitboxes.size());
            for (Map.Entry<String, Rectangle> value : animationData.hitboxes.entrySet()) {
                Rectangle rectangle = value.getValue();
                shape.setAsBox(rectangle.width * scale, rectangle.height * scale, new Vector2(flippedFactor * rectangle.x, rectangle.y).scl(scale), 0f);
                Fixture fixture = body.createFixture(def);
                fixture.setUserData(entity);
                hitboxes.add(new BodyComponent.Box(value.getKey(), fixture));
            }
            def.filter.categoryBits = Bits.Category.HURTBOX.bits;
            def.filter.maskBits = Bits.Mask.HURTBOX.bits;
            for (Map.Entry<String, Rectangle> value : animationData.hurtboxes.entrySet()) {
                Rectangle rectangle = value.getValue();
                shape.setAsBox(rectangle.width * scale, rectangle.height * scale, new Vector2(flippedFactor * rectangle.x, rectangle.y).scl(scale), 0f);
                Fixture fixture = body.createFixture(def);
                fixture.setUserData(entity);
            }
            shape.dispose();
            return new BodyComponent(body, hitboxes);
        }));
        entity.add(new ActorComponent(image, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequestComponent(name, controls, player));
        return entity;
    }

    public ArrayList<Entity> backgroundLayers() {
        String[] bgLayers = new String[]{
                "sky",
                "mountains",
                "bg trees",
                "mg trees",
                "bg bg characters",
                "ground",
        };
        String[] fgLayers = new String[]{
                "fg ground",
                "fg crowd",
        };
        ArrayList<Entity> layers = new ArrayList<>(8);

        for (String layer : bgLayers) {
            layers.add(createLayer(layer, ActorComponent.Layer.BG_PARALLAX));
        }

        for (String layer : fgLayers) {
            layers.add(createLayer(layer, ActorComponent.Layer.FG_PARALLAX));
        }
        return layers;
    }

    private Entity createLayer(String name, ActorComponent.Layer layer) {
        Entity entity = new Entity();
        Image image = new Image(atlas.findRegion("arena/arena 0/layers/arena 0_" + name));
        image.setScale(config.scale);
        image.setPosition(0, 0);
        entity.add(new ActorComponent(image, layer));
        return entity;
    }

    public Entity base() {
        Entity entity = new Entity();
        Vector2 initialPosition = new Vector2(config.width / 2, 1f);
        entity.add(platformBuilder(initialPosition));
        return entity;
    }

    private BodyBuilderComponent platformBuilder(Vector2 position) {
        return new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(position);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(Platform.Width.SIXTEEN.width / 2f, 0.25f);
            body.createFixture(shape, 0.0f).setUserData(UserData.PLATFORM);
            shape.dispose();
            return new BodyComponent(body);
        });
    }
}
