package io.github.fourlastor.game.di.modules;

import dagger.Module;
import io.github.fourlastor.game.level.di.LevelComponent;

@Module(
        subcomponents = {
            LevelComponent.class,
        })
public class ScreensModule {}
