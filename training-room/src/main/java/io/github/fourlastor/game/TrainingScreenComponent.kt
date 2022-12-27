package io.github.fourlastor.game

import dagger.Subcomponent
import io.github.fourlastor.game.di.ScreenScoped
import io.github.fourlastor.game.level.PlayerAnimationsFactory

@ScreenScoped
@Subcomponent(modules = [TrainingModule::class, PlayerAnimationsFactory::class])
interface TrainingScreenComponent {
    fun screen(): TrainingScreen

    @Subcomponent.Builder
    interface Builder {
        fun build(): TrainingScreenComponent
    }
}
