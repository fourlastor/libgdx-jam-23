package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.utils.JsonReader;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class JsonModule {

    @Provides
    @Singleton
    public JsonReader jsonReader() {
        return new JsonReader();
    }
}
