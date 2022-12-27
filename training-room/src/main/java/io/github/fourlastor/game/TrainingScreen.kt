package io.github.fourlastor.game

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.physics.box2d.World
import io.github.fourlastor.game.component.BodyComponent
import io.github.fourlastor.game.component.HpComponent
import io.github.fourlastor.game.component.InputComponent
import io.github.fourlastor.game.component.PlayerComponent
import io.github.fourlastor.game.level.EntitiesFactory
import io.github.fourlastor.game.level.Match
import io.github.fourlastor.game.level.Player
import io.github.fourlastor.game.level.input.controls.Command
import io.github.fourlastor.game.level.input.controls.Controls
import io.github.fourlastor.rpc.Action
import io.github.fourlastor.rpc.ActionType
import io.github.fourlastor.rpc.Actions
import io.github.fourlastor.rpc.GameInfo
import io.github.fourlastor.rpc.PlayerInfo
import io.github.fourlastor.rpc.Position
import javax.inject.Inject

class TrainingScreen @Inject constructor(
    private val engine: Engine,
    private val entitiesFactory: EntitiesFactory,
    private val world: World,
    match: Match,
    private val inputs: ComponentMapper<InputComponent>,
    private val players: ComponentMapper<PlayerComponent>,
    private val bodies: ComponentMapper<BodyComponent>,
    private val hps: ComponentMapper<HpComponent>,
) : ScreenAdapter() {
    private val p2IsImpostor: Boolean = match.p1 == match.p2

    override fun render(delta: Float) {
        engine.update(delta)
    }

    private val p1 = entitiesFactory.player(match.p1, Controls.Setup.P1, Player.P1)
    private val p2 = entitiesFactory.player(match.p2, Controls.Setup.P2, Player.P2, p2IsImpostor)

    override fun show() {
        for (layer in entitiesFactory.backgroundLayers()) {
            engine.addEntity(layer)
        }
        engine.addEntity(entitiesFactory.base())
        engine.addEntity(p1)
        engine.addEntity(p2)
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
        inputs.get(p1).command = actions.p1.asCommand()
        inputs.get(p2).command = actions.p2.asCommand()
        render(1f / 60f)
    }

    fun state(): GameInfo {
        return GameInfo.newBuilder().apply {
            p1 = playerInfo(this@TrainingScreen.p1)
            p2 = playerInfo(this@TrainingScreen.p2)
        }.build()
    }

    private fun playerInfo(entity: Entity): PlayerInfo? {
        val playerComponent = players.get(entity)
        val body = bodies.get(entity).body
        val hp = hps.get(entity)
        val command = inputs.get(entity).command
        return PlayerInfo.newBuilder().apply {
            player = if (playerComponent.player.flipped) {
                io.github.fourlastor.rpc.Player.P2
            } else {
                io.github.fourlastor.rpc.Player.P1
            }
            position = Position.newBuilder().apply {
                x = body.position.x
                y = body.position.y
            }.build()

            health = hp.hp.toFloat() / hp.maxHp.toFloat()
            currentAction = command.asAction()
        }.build()
    }
}

private fun Action.asCommand(): Command = when (this.type) {
    null -> Command.NONE
    ActionType.None -> Command.NONE
    ActionType.Left -> Command.LEFT
    ActionType.Right -> Command.RIGHT
    ActionType.Attack -> Command.ATTACK
    ActionType.UNRECOGNIZED -> Command.NONE
}

private fun Command.asAction(): ActionType = when (this) {
    Command.NONE -> ActionType.None
    Command.ATTACK -> ActionType.Attack
    Command.LEFT -> ActionType.Left
    Command.RIGHT -> ActionType.Right
}
