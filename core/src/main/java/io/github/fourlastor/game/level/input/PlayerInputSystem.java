package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import io.github.fourlastor.game.component.AnimationFinishedComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyComponent;
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

public class PlayerInputSystem extends IteratingSystem {

    private static final Family FAMILY_REQUEST =
            Family.all(PlayerRequestComponent.class, BodyComponent.class).get();
    private static final Family FAMILY_ANIMATION_FINISHED =
            Family.all(AnimationFinishedComponent.class).get();
    private static final Family FAMILY = Family.all(
                    PlayerComponent.class, BodyComponent.class, AnimationImageComponent.class)
            .get();

    private final InputMultiplexer inputMultiplexer;
    private final PlayerSetup playerSetup;
    private final ComponentMapper<PlayerComponent> players;

    @Inject
    public PlayerInputSystem(
            InputMultiplexer inputMultiplexer, PlayerSetup playerSetup, ComponentMapper<PlayerComponent> players) {
        super(FAMILY);
        this.inputMultiplexer = inputMultiplexer;
        this.playerSetup = playerSetup;
        this.players = players;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        players.get(entity).stateMachine.update();
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
        inputMultiplexer.removeProcessor(inputProcessor);
        super.removedFromEngine(engine);
    }

    private final EntityListener enableOnAnimationFinished = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
            setProcessing(true);
            inputMultiplexer.addProcessor(inputProcessor);
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
        private final InputStateMachine.Factory stateMachineFactory;
        private final MessageDispatcher messageDispatcher;

        @Inject
        public PlayerSetup(
                Idle.Factory idleFactory,
                Walking.Factory walkingFactory,
                Attacking.Factory attackingFactory,
                Hurt.Factory hurtFactory, InputStateMachine.Factory stateMachineFactory,
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

            String name = request.fighter.charName;
            Controls controls = request.controls;
            Player player = request.player;
            Idle idle = idleFactory.create(name, controls);
            Walking walking = walkingFactory.create(name, controls);
            Attacking attacking = attackingFactory.create(name, controls);
            Hurt hurt = hurtFactory.create(name, controls);
            InputStateMachine stateMachine = stateMachineFactory.create(entity, idle);
            entity.add(new PlayerComponent(stateMachine, idle, walking, attacking, hurt, player));
            stateMachine.getCurrentState().enter(entity);
            for (Message value : Message.values()) {
                messageDispatcher.addListener(stateMachine, value.ordinal());
            }
        }

        @Override
        public void entityRemoved(Entity entity) {
        }
    }

    /**
     * Forwards the input to the current state.
     */
    private final InputProcessor inputProcessor = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
            for (Entity entity : getEntities()) {
                if (players.get(entity).stateMachine.keyDown(entity, keycode)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            for (Entity entity : getEntities()) {
                if (players.get(entity).stateMachine.keyUp(entity, keycode)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            for (Entity entity : getEntities()) {
                if (players.get(entity).stateMachine.touchDown(entity, screenX, screenY, pointer, button)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            for (Entity entity : getEntities()) {
                if (players.get(entity).stateMachine.touchUp(entity, screenX, screenY, pointer, button)) {
                    return true;
                }
            }
            return false;
        }
    };
}
