package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.fourlastor.game.animation.EntityAnimationDataParser;
import io.github.fourlastor.game.animation.EntityJsonParser;
import io.github.fourlastor.game.animation.json.EntityData;
import io.github.fourlastor.game.animation.scene2d.AnimatedValue;
import io.github.fourlastor.game.animation.scene2d.AnimationImage;
import io.github.fourlastor.game.component.ActorComponent;
import io.github.fourlastor.game.component.AnimatedImageComponent;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.PlayerRequestComponent;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.blueprint.definitions.Platform;
import io.github.fourlastor.game.ui.AnimatedImage;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private static final float CHARACTER_SCALE_XY = 1f;
    private final Animation<TextureRegion> whitePixel;
    private final EntityAnimationDataParser animationDataParser;
    private final EntityJsonParser jsonParser;

    @Inject
    public EntitiesFactory(
            @Named(PlayerAnimationsFactory.WHITE_PIXEL) Animation<TextureRegion> whitePixel,
            EntityAnimationDataParser animationDataParser, EntityJsonParser jsonParser) {
        this.whitePixel = whitePixel;
        this.animationDataParser = animationDataParser;
        this.jsonParser = jsonParser;
    }

    public Entity player() {
        Entity entity = new Entity();
        AnimatedImage image = new AnimatedImage(whitePixel);
        image.setScale(CHARACTER_SCALE_XY);

        entity.add(new AnimatedImageComponent(image));
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(4.5f, 1.5f));
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1f, 1f);
            Fixture fixture = body.createFixture(shape, 0.0f);
//            fixture.setFriction(100f);
            fixture.setRestitution(0.15f);
            fixture.setUserData(UserData.PLAYER);
            shape.dispose();
            return body;
        }));
        image.setPosition(-0.5f, -0.5f);
        Group group = new Group();
        group.addActor(image);

        EntityData entityData = jsonParser.parse(Gdx.files.internal("entities/kick_test.json"));
        AnimatedValue<TextureRegionDrawable> animatedValue = animationDataParser.parseCharacter(entityData);

        AnimationImage actor = new AnimationImage(animatedValue);
        entity.add(new ActorComponent(actor, ActorComponent.Layer.CHARACTER));
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
