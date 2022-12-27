package io.github.fourlastor.game

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.physics.box2d.World
import io.github.fourlastor.game.level.EntitiesFactory
import io.github.fourlastor.game.level.Match
import io.github.fourlastor.game.level.Player
import io.github.fourlastor.game.level.input.controls.Controls
import io.github.fourlastor.rpc.Actions
import io.github.fourlastor.rpc.GameInfo
import javax.inject.Inject

class TrainingScreen @Inject constructor(
    private val engine: Engine,
    private val entitiesFactory: EntitiesFactory,
    private val world: World,
    private val match: Match,
) : ScreenAdapter() {
    private val p2IsImpostor: Boolean = match.p1 == match.p2

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun show() {
        for (layer in entitiesFactory.backgroundLayers()) {
            engine.addEntity(layer)
        }
        engine.addEntity(entitiesFactory.base())
        engine.addEntity(entitiesFactory.player(match.p1, Controls.Setup.P1, Player.P1))
        engine.addEntity(entitiesFactory.player(match.p2, Controls.Setup.P2, Player.P2, p2IsImpostor))
    }

    override fun hide() {
        engine.removeAllEntities()
        engine.removeAllSystems()
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
    }

    fun update(actions: Actions) {
        TODO("Not yet implemented")
    }

    fun state(): GameInfo {
        TODO("Not yet implemented")
    }
}
