package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
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
        assetManager.load("music/character_selection_bg.mp3", Music.class);
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
