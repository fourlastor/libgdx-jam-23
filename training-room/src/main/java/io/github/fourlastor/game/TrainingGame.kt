package io.github.fourlastor.game

import com.badlogic.gdx.Game
import io.github.fourlastor.rpc.Actions
import io.github.fourlastor.rpc.GameInfo
import javax.inject.Inject

class TrainingGame @Inject constructor(
    screenComponent: TrainingScreenComponent.Builder,
) : Game() {

    private val trainingScreen: TrainingScreen by lazy { screenComponent.build().screen() }

    fun update(actions: Actions) {
        trainingScreen.update(actions)
    }

    fun state(): GameInfo {
        return trainingScreen.state()
    }

    override fun create() {
        setScreen(trainingScreen)
    }
}
