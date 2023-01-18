package io.github.fourlastor.game

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ScreenAdapter
import io.github.fourlastor.game.component.AnimationFinishedComponent
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
import io.github.fourlastor.rpc.Fighter as RpcFighter

class TrainingScreen @Inject constructor(
    private val engine: Engine,
    private val entitiesFactory: EntitiesFactory,
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
        /** [io.github.fourlastor.game.level.input.CharacterStateSystem] waits for the UI to disappear. */
        engine.addEntity(Entity().apply { add(AnimationFinishedComponent()) })
        engine.addEntity(entitiesFactory.base())
        engine.addEntity(p1)
        engine.addEntity(p2)
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
            fighter = playerComponent.fighter.toRpc()
            position = Position.newBuilder().apply {
                x = body.position.x
                y = body.position.y
            }.build()

            health = hp.hp.toFloat() / hp.maxHp.toFloat()
            currentAction = command.asAction()
        }.build()
    }

    override fun dispose() {
        engine.removeAllEntities()
        super.dispose()
    }
}

private fun Fighter.toRpc(): RpcFighter = when (this) {
    Fighter.NISSEFAR -> RpcFighter.Nissefar
    Fighter.LANGNISSE -> RpcFighter.Langnisse
    Fighter.NISSEMOR -> RpcFighter.Nissemor
}

private fun Action.asCommand(): Command = when (this.type) {
    null -> Command.IDLE
    ActionType.Idle -> Command.IDLE
    ActionType.Left -> Command.LEFT
    ActionType.Right -> Command.RIGHT
    ActionType.Attack -> Command.ATTACK
    ActionType.UNRECOGNIZED -> Command.IDLE
}

private fun Command.asAction(): ActionType = when (this) {
    Command.IDLE -> ActionType.Idle
    Command.ATTACK -> ActionType.Attack
    Command.LEFT -> ActionType.Left
    Command.RIGHT -> ActionType.Right
}
