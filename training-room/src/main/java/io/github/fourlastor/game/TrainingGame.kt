package io.github.fourlastor.game

import com.badlogic.gdx.Game
import io.github.fourlastor.rpc.Actions
import io.github.fourlastor.rpc.GameInfo
import javax.inject.Inject

class TrainingGame @Inject constructor(
    private val screenComponent: TrainingScreenComponent.Builder,
) : Game() {

    private lateinit var trainingScreen: TrainingScreen

    fun update(actions: Actions) {
        trainingScreen.update(actions)
    }

    fun state(): GameInfo {
        return trainingScreen.state()
    }

    fun reset() {
        trainingScreen = screenComponent.build().screen()
        setScreen(trainingScreen)
    }

    override fun create() {
        reset()
    }
}
