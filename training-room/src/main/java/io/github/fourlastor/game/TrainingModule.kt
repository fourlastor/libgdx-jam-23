package io.github.fourlastor.game

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.msg.MessageDispatcher
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import dagger.Module
import dagger.Provides
import io.github.fourlastor.game.di.ScreenScoped
import io.github.fourlastor.game.level.Match
import io.github.fourlastor.game.level.Round
import io.github.fourlastor.game.level.WorldConfig
import io.github.fourlastor.game.level.di.Gravity
import io.github.fourlastor.game.level.input.CharacterStateSystem
import io.github.fourlastor.game.level.physics.PhysicsSystem
import io.github.fourlastor.game.level.system.ActorFollowBodySystem
import io.github.fourlastor.game.level.system.CameraMovementSystem
import io.github.fourlastor.game.level.system.ShadowFollowBodySystem

@Module
class TrainingModule {

    @Provides
    fun match(): Match {
        return Match(
            Round.ROUND_1,
            Fighter.LANGNISSE,
            Fighter.NISSEFAR,
            null,
        )
    }

    @Provides
    @ScreenScoped
    fun engine(
        characterStateSystem: CharacterStateSystem,
        physicsSystem: PhysicsSystem,
        actorFollowBodySystem: ActorFollowBodySystem,
        shadowFollowBodySystem: ShadowFollowBodySystem,
        cameraMovementSystem: CameraMovementSystem,
    ): Engine {
        val engine = Engine()
        engine.addSystem(characterStateSystem)
        engine.addSystem(shadowFollowBodySystem)
        engine.addSystem(physicsSystem)
        engine.addSystem(actorFollowBodySystem)
        engine.addSystem(cameraMovementSystem)
        return engine
    }

    @Provides
    @ScreenScoped
    fun worldConfig(): WorldConfig {
        return WorldConfig(16f, 9f, 13f / 256f)
    }

    @Provides
    @ScreenScoped
    fun camera(): Camera {
        return OrthographicCamera(16f, 9f)
    }

    @Provides
    @ScreenScoped
    @Gravity
    fun gravity(): Vector2 {
        return Vector2(0f, -10f)
    }

    @Provides
    @ScreenScoped
    fun world(@Gravity gravity: Vector2): World {
        return World(gravity, true)
    }

    @Provides
    @ScreenScoped
    fun messageDispatcher(): MessageDispatcher {
        return MessageDispatcher()
    }
}
