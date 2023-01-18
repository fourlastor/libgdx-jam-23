package io.github.fourlastor.game.level.di;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.AnimationUpdateSystem;
import io.github.fourlastor.game.level.Match;
import io.github.fourlastor.game.level.WorldConfig;
import io.github.fourlastor.game.level.input.CharacterStateSystem;
import io.github.fourlastor.game.level.input.InputBufferSystem;
import io.github.fourlastor.game.level.physics.PhysicsDebugSystem;
import io.github.fourlastor.game.level.physics.PhysicsSystem;
import io.github.fourlastor.game.level.system.ActorFollowBodySystem;
import io.github.fourlastor.game.level.system.CameraMovementSystem;
import io.github.fourlastor.game.level.system.ClearScreenSystem;
import io.github.fourlastor.game.level.system.GoToNextScreenSystem;
import io.github.fourlastor.game.level.system.ShadowFollowBodySystem;
import io.github.fourlastor.game.level.system.SoundSystem;
import io.github.fourlastor.game.level.system.StageSystem;
import io.github.fourlastor.game.level.system.UiSystem;

@Module
public class LevelModule {


    private final Match match;

    public LevelModule(Match match) {
        this.match = match;
    }

    @Provides
    public Match match() {
        return match;
    }

    @Provides
    @ScreenScoped
    public Engine engine(
            CharacterStateSystem characterStateSystem,
            PhysicsSystem physicsSystem,
            ActorFollowBodySystem actorFollowBodySystem,
            StageSystem stageSystem,
            ClearScreenSystem clearScreenSystem,
            @SuppressWarnings("unused") // debug only
            PhysicsDebugSystem physicsDebugSystem,
            SoundSystem soundSystem,
            UiSystem uiSystem,
            GoToNextScreenSystem goToNextScreenSystem,
            CameraMovementSystem cameraMovementSystem,
            InputBufferSystem inputBufferSystem,
            AnimationUpdateSystem animationUpdateSystem,
            ShadowFollowBodySystem shadowFollowBodySystem
    ) {
        Engine engine = new Engine();
        engine.addSystem(inputBufferSystem);
        engine.addSystem(characterStateSystem);
        engine.addSystem(shadowFollowBodySystem);
        engine.addSystem(animationUpdateSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(soundSystem);
        engine.addSystem(actorFollowBodySystem);
        engine.addSystem(clearScreenSystem);
        engine.addSystem(cameraMovementSystem);
        engine.addSystem(stageSystem);
        engine.addSystem(uiSystem);
        engine.addSystem(goToNextScreenSystem);
//        engine.addSystem(physicsDebugSystem);
        return engine;
    }

    @Provides
    @ScreenScoped
    public WorldConfig worldConfig() {
        return new WorldConfig(16f, 9f, 13f / 256f);
    }

    @Provides
    @ScreenScoped
    public Viewport viewport(WorldConfig config) {
        return new FitViewport(config.width, config.height);
    }

    @Provides
    @ScreenScoped
    public Stage stage(Viewport viewport) {
        return new Stage(viewport);
    }

    @Provides
    @ScreenScoped
    public Camera camera(Viewport viewport) {
        return viewport.getCamera();
    }

    @Provides
    @ScreenScoped
    @Gravity
    public Vector2 gravity() {
        return new Vector2(0f, -10f);
    }

    @Provides
    @ScreenScoped
    public World world(@Gravity Vector2 gravity) {
        return new World(gravity, true);
    }

    @Provides
    @ScreenScoped
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }
}
