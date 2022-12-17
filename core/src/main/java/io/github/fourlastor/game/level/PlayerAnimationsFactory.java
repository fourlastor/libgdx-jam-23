package io.github.fourlastor.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.github.fourlastor.game.animation.EntityAnimationDataParser;
import io.github.fourlastor.game.animation.EntityJsonParser;
import io.github.fourlastor.game.animation.data.CharacterAnimationData;
import io.github.fourlastor.game.animation.json.EntityData;
import io.github.fourlastor.game.di.ScreenScoped;

import javax.inject.Named;

@Module
public class PlayerAnimationsFactory {
    public static final String KARATENISSE = "karatenisse";
    public static final String NISSEMOR = "nissemor";
    public static final String LANGNISSE = "langnisse";

    @Provides
    @ScreenScoped
    @Named(KARATENISSE)
    public EntityData karatenisseEntity(EntityJsonParser parser) {
        return parser.parse(animationJson(KARATENISSE));
    }

    @Provides
    @ScreenScoped
    @Named(NISSEMOR)
    public EntityData nissemorEntity(EntityJsonParser parser) {
        return parser.parse(animationJson(NISSEMOR));
    }

    @Provides
    @ScreenScoped
    @Named(LANGNISSE)
    public EntityData langnisseEntity(EntityJsonParser parser) {
        return parser.parse(animationJson(LANGNISSE));
    }

    private FileHandle animationJson(String name) {
        return Gdx.files.internal("images/included/animations/" + name + "/" + name + ".json");
    }

    @Provides
    @ScreenScoped
    @StringKey(KARATENISSE)
    @IntoMap
    public CharacterAnimationData karatenisseAnimation(
            EntityAnimationDataParser parser,
            @Named(KARATENISSE) EntityData data
    ) {
        return parser.parseCharacterData(data, KARATENISSE);
    }

    @Provides
    @ScreenScoped
    @StringKey(NISSEMOR)
    @IntoMap
    public CharacterAnimationData nissemorAnimation(
            EntityAnimationDataParser parser,
            @Named(NISSEMOR) EntityData data
    ) {
        return parser.parseCharacterData(data, NISSEMOR);
    }

    @Provides
    @ScreenScoped
    @StringKey(LANGNISSE)
    @IntoMap
    public CharacterAnimationData langnisseAnimation(
            EntityAnimationDataParser parser,
            @Named(LANGNISSE) EntityData data
    ) {
        return parser.parseCharacterData(data, LANGNISSE);
    }
}
