package io.github.fourlastor.rpc

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import io.github.fourlastor.game.TrainingGame
import javax.inject.Inject

class GameService @Inject constructor(
    private val game: TrainingGame,
) : GameGrpcKt.GameCoroutineImplBase() {

    init {
        val config = HeadlessApplicationConfiguration().apply {
            updatesPerSecond = -1
        }
        
        @Suppress("UNUSED_VARIABLE") // This is to set up Gdx.xxx which requires an application
        val app = HeadlessApplication(
            game,
            config
        )
    }

    override suspend fun update(request: Actions): GameInfo {
        return game.run {
            update(request)
            state()
        }
    }
}
