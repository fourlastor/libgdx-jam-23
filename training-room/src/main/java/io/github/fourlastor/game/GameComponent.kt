package io.github.fourlastor.game

import dagger.Component
import io.github.fourlastor.game.di.modules.EcsModule
import io.github.fourlastor.game.di.modules.GameModule
import io.github.fourlastor.game.di.modules.JsonModule
import io.github.fourlastor.rpc.GameService
import javax.inject.Singleton

@Singleton
@Component(modules = [GameModule::class, EcsModule::class, JsonModule::class, ScreensModule::class])
interface GameComponent {

    fun service(): GameService

    companion object {
        fun component(): GameComponent {
            return DaggerGameComponent.create()
        }
    }
}
