package io.github.fourlastor.game.di.modules;

import dagger.Module;
import io.github.fourlastor.game.level.di.LevelComponent;
import io.github.fourlastor.game.selection.CharacterSelectionComponent;

@Module(
        subcomponents = {
                LevelComponent.class,
                CharacterSelectionComponent.class,
        })
public class ScreensModule {
}
