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
import dagger.Lazy;
import io.github.fourlastor.game.Fighter;
import io.github.fourlastor.game.MyGdxGame;
import io.github.fourlastor.game.animation.AnimationImage;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.component.ActorComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.InputComponent;
import io.github.fourlastor.game.component.PlayerRequestComponent;
import io.github.fourlastor.game.component.ShadowComponent;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.input.controls.Controls;
import io.github.fourlastor.game.level.physics.Bits;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private final WorldConfig config;
    private final Map<String, CharacterAnimationData> animations;
    private final Lazy<TextureAtlas> atlas;

    @Inject
    public EntitiesFactory(WorldConfig config, Map<String, CharacterAnimationData> animations, Lazy<TextureAtlas> atlas) {
        this.config = config;
        this.animations = animations;
        this.atlas = atlas;
    }

    public Entity player(Fighter fighter,
                         Controls controls,
                         Player player) {
        return player(fighter, controls, player, false);
    }

    public Entity player(Fighter fighter,
                         Controls controls,
                         Player player,
                         boolean isImpostor) {
        CharacterAnimationData animationData = animations.get(fighter.charName);
        Entity entity = new Entity();
        entity.add(new HpComponent());
        AnimationImage image = new AnimationImage();
        if (isImpostor) {
            image.setColor(MyGdxGame.IMPOSTOR_COLOR);
        }
        float scale = config.scale;
        int flippedFactor = player.flipped ? -1 : 1;
        image.setScale(flippedFactor * scale, scale);
        entity.add(new InputComponent());
        entity.add(new AnimationImageComponent(image));
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            float x = player.flipped ? 15f : 1f;
            float height = animationData.height * config.scale;
            Vector2 initialPosition = new Vector2(x, height);
            bodyDef.position.set(initialPosition);
            bodyDef.allowSleep = false;
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.4f, 0.4f, new Vector2(0f, (-height / 2) + 0.4f), 0f);
            FixtureDef def = new FixtureDef();
            def.shape = shape;
            def.filter.categoryBits = Bits.Category.BODY.bits;
            def.filter.maskBits = Bits.Mask.BODY.bits;
            body.createFixture(def).setUserData(entity);

            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(initialPosition);
            Body shadow = world.createBody(bodyDef);
            def = new FixtureDef();
            def.shape = shape;
            def.filter.categoryBits = Bits.Category.SHADOW.bits;
            def.filter.maskBits = Bits.Mask.SHADOW.bits;
            shadow.createFixture(def).setUserData(entity);

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
            return Arrays.asList(
                    new BodyComponent(body, hitboxes),
                    new ShadowComponent(shadow)
            );
        }));
        entity.add(new ActorComponent(image, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequestComponent(fighter, controls, player));
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
        Image image = new Image(atlas.get().findRegion("arena/arena 0/layers/arena 0_" + name));
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
            shape.setAsBox(50f, 0.25f);
            FixtureDef def = new FixtureDef();
            def.filter.categoryBits = Bits.Category.GROUND.bits;
            def.filter.maskBits = Bits.Mask.GROUND.bits;
            def.shape = shape;
            body.createFixture(def).setUserData(UserData.PLATFORM);
            shape.dispose();
            return Collections.singletonList(new BodyComponent(body));
        });
    }
}
