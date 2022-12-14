package io.github.fourlastor.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.animation.EntityAnimationDataParser;
import io.github.fourlastor.game.animation.EntityJsonParser;
import io.github.fourlastor.game.animation.data.AnimatedValue;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.animation.json.EntityData;
import io.github.fourlastor.game.di.ScreenScoped;

import javax.inject.Named;

@Module
public class PlayerAnimationsFactory {

    public static final String WHITE_PIXEL = "whitePixel";
    public static final String ANIMATION_IDLE = "idle";
    public static final String ANIMATION_KICK = "kick";
    public static final String ANIMATION_FALLING = "player/Falling/Falling";
    public static final String ANIMATION_JUMPING = "player/Jumping/Jumping";
    public static final String ANIMATION_CHARGE_JUMP = "player/ChargeJump/ChargeJump";
    private static final float FRAME_DURATION = 0.1f;
    public static final String KARATENISSE = "karatenisse";

    @Provides
    @ScreenScoped
    @Named(KARATENISSE)
    public EntityData entityData(EntityJsonParser parser) {
        return parser.parse(Gdx.files.internal("entities/kick_test.json"));
    }

    @Provides
    @ScreenScoped
    @Named(KARATENISSE)
    public CharacterAnimationData characterAnimationData(
            EntityAnimationDataParser parser,
            @Named(KARATENISSE) EntityData data
    ) {
        return parser.parseCharacterData(data);
    }

    @Provides
    @ScreenScoped
    @Named(ANIMATION_IDLE)
    public AnimatedValue<TextureRegionDrawable> idle(
            EntityAnimationDataParser parser,
            @Named(KARATENISSE) EntityData entityData
    ) {
        return parser.parseAnimation(entityData, "idle");
    }

    @Provides
    @ScreenScoped
    @Named(ANIMATION_KICK)
    public AnimatedValue<TextureRegionDrawable> kick(
            EntityAnimationDataParser parser,
            @Named(KARATENISSE) EntityData entityData
    ) {
        return parser.parseAnimation(entityData, "kick");
    }

    @Provides
    @ScreenScoped
    @Named(ANIMATION_IDLE)
    public Animation<TextureRegion> standing(TextureAtlas textureAtlas) {
        return new Animation<>(FRAME_DURATION, textureAtlas.findRegions(ANIMATION_IDLE), Animation.PlayMode.LOOP);
    }

    @Provides
    @ScreenScoped
    @Named(WHITE_PIXEL)
    public Animation<TextureRegion> whitePixel(TextureAtlas textureAtlas) {
        return new Animation<>(FRAME_DURATION, textureAtlas.findRegions(WHITE_PIXEL), Animation.PlayMode.LOOP);
    }

    @Provides
    @ScreenScoped
    @Named(ANIMATION_FALLING)
    public Animation<TextureRegion> falling(TextureAtlas textureAtlas) {
        return new Animation<>(FRAME_DURATION, textureAtlas.findRegions(ANIMATION_FALLING), Animation.PlayMode.NORMAL);
    }

    @Provides
    @ScreenScoped
    @Named(ANIMATION_JUMPING)
    public Animation<TextureRegion> jumping(TextureAtlas textureAtlas) {
        return new Animation<>(FRAME_DURATION, textureAtlas.findRegions(ANIMATION_JUMPING), Animation.PlayMode.NORMAL);
    }

    @Provides
    @ScreenScoped
    @Named(ANIMATION_CHARGE_JUMP)
    public Animation<TextureRegion> chargeJump(TextureAtlas textureAtlas) {
        return new Animation<>(
                FRAME_DURATION, textureAtlas.findRegions(ANIMATION_CHARGE_JUMP), Animation.PlayMode.NORMAL);
    }
}
