package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import io.github.fourlastor.game.Fighter;
import io.github.fourlastor.game.component.AnimationFinishedComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.InputComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.component.PlayerRequestComponent;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.level.input.controls.Controls;
import io.github.fourlastor.game.level.input.state.Attacking;
import io.github.fourlastor.game.level.input.state.Hurt;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.game.level.input.state.Walking;

import javax.inject.Inject;

public class CharacterStateSystem extends IteratingSystem {

    private static final Family FAMILY_REQUEST =
            Family.all(PlayerRequestComponent.class, BodyComponent.class).get();
    private static final Family FAMILY_ANIMATION_FINISHED =
            Family.all(AnimationFinishedComponent.class).get();
    private static final Family FAMILY = Family.all(
                    PlayerComponent.class, BodyComponent.class, AnimationImageComponent.class, InputComponent.class)
            .get();

    private final PlayerSetup playerSetup;
    private final ComponentMapper<PlayerComponent> players;

    @Inject
    public CharacterStateSystem(PlayerSetup playerSetup, ComponentMapper<PlayerComponent> players) {
        super(FAMILY);
        this.playerSetup = playerSetup;
        this.players = players;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        players.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY_REQUEST, playerSetup);
        setProcessing(false);
        engine.addEntityListener(FAMILY_ANIMATION_FINISHED, enableOnAnimationFinished);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(playerSetup);
        engine.removeEntityListener(enableOnAnimationFinished);
        super.removedFromEngine(engine);
    }

    private final EntityListener enableOnAnimationFinished = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
            setProcessing(true);
        }

        @Override
        public void entityRemoved(Entity entity) {

        }
    };

    /**
     * Creates a player component whenever a request to set up a player is made.
     * Takes care of instantiating the state machine and the possible player states.
     */
    public static class PlayerSetup implements EntityListener {

        private final Idle.Factory idleFactory;
        private final Walking.Factory walkingFactory;
        private final Attacking.Factory attackingFactory;
        private final Hurt.Factory hurtFactory;
        private final CharacterStateMachine.Factory stateMachineFactory;
        private final MessageDispatcher messageDispatcher;

        @Inject
        public PlayerSetup(
                Idle.Factory idleFactory,
                Walking.Factory walkingFactory,
                Attacking.Factory attackingFactory,
                Hurt.Factory hurtFactory, CharacterStateMachine.Factory stateMachineFactory,
                MessageDispatcher messageDispatcher) {
            this.idleFactory = idleFactory;
            this.walkingFactory = walkingFactory;
            this.attackingFactory = attackingFactory;
            this.hurtFactory = hurtFactory;
            this.stateMachineFactory = stateMachineFactory;
            this.messageDispatcher = messageDispatcher;
        }

        @Override
        public void entityAdded(Entity entity) {
            PlayerRequestComponent request = entity.remove(PlayerRequestComponent.class);

            Fighter fighter = request.fighter;
            String name = fighter.charName;
            Controls controls = request.controls;
            Player player = request.player;
            Idle idle = idleFactory.create(name);
            Walking walking = walkingFactory.create(name);
            Attacking attacking = attackingFactory.create(name);
            Hurt hurt = hurtFactory.create(name);
            CharacterStateMachine stateMachine = stateMachineFactory.create(entity, idle);
            entity.add(new PlayerComponent(
                    controls,
                    stateMachine,
                    idle,
                    walking,
                    attacking,
                    hurt,
                    player,
                    fighter
            ));
            stateMachine.getCurrentState().enter(entity);
            for (Message value : Message.values()) {
                messageDispatcher.addListener(stateMachine, value.ordinal());
            }
        }

        @Override
        public void entityRemoved(Entity entity) {
        }
    }

}
