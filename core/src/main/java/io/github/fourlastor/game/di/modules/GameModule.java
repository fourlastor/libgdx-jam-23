package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.MyGdxGame;
import io.github.fourlastor.game.level.di.LevelComponent;
import io.github.fourlastor.game.selection.CharacterSelectionComponent;
import io.github.fourlastor.game.util.Text;
import io.github.fourlastor.game.util.TextLoader;

import javax.inject.Singleton;
import java.util.Random;

@Module
public class GameModule {

    private static final String PATH_TEXTURE_ATLAS = "images/included/packed/images.pack.atlas";
    public static final String SELECT_CHANGE = "audio/sound/character selection screen/select-change.wav";
    public static final String SELECT_FAIL = "audio/sound/character selection screen/select-fail.wav";
    public static final String SELECT_SUCCESS = "audio/sound/character selection screen/select-success.wav";
    public static final String HIT = "audio/sound/arena/hit.wav";
    public static final String CHARACTER_SELECTION_BG = "audio/music/character_selection_bg.mp3";
    public static final String ARENA_BG = "audio/music/drawn_to_fight_-_title_screen.ogg";

    @Provides
    @Singleton
    public AssetManager assetManager() {
        AssetManager assetManager = new AssetManager();
        assetManager.setLoader(
                Text.class,
                new TextLoader(
                        new InternalFileHandleResolver()
                )
        );
        assetManager.load(PATH_TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.load(CHARACTER_SELECTION_BG, Music.class);
        assetManager.load(ARENA_BG, Music.class);
        assetManager.load(SELECT_CHANGE, Sound.class);
        assetManager.load(SELECT_FAIL, Sound.class);
        assetManager.load(SELECT_SUCCESS, Sound.class);
        assetManager.load(HIT, Sound.class);
        assetManager.load(new AssetDescriptor<>("shaders/default.vs", Text.class, new TextLoader.TextParameter()));
        assetManager.load(new AssetDescriptor<>("shaders/wave.fs", Text.class, new TextLoader.TextParameter()));
        assetManager.finishLoading();

        return assetManager;
    }

    @Provides
    @Singleton
    public InputMultiplexer inputMultiplexer() {
        return new InputMultiplexer();
    }

    @Provides
    @Singleton
    public TextureAtlas textureAtlas(AssetManager assetManager) {
        return assetManager.get(PATH_TEXTURE_ATLAS, TextureAtlas.class);
    }

    @Provides
    @Singleton
    public MyGdxGame game(
            InputMultiplexer multiplexer,
            LevelComponent.Builder levelBuilder,
            CharacterSelectionComponent.Builder characterSelectionFactory
    ) {
        return new MyGdxGame(multiplexer, levelBuilder, characterSelectionFactory);
    }

    @Provides
    public Random random() {
        return new Random();
    }
}
