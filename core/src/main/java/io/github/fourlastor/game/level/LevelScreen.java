package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.di.modules.GameModule;
import io.github.fourlastor.game.level.input.controls.Controls;

import javax.inject.Inject;

public class LevelScreen extends ScreenAdapter {

    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;
    private final Music music;

    private final World world;
    private final Match match;
    private final boolean p2IsImpostor;

    private final InputMultiplexer multiplexer;

    @Inject
    public LevelScreen(
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            World world,
            Match match,
            AssetManager assetManager,
            InputMultiplexer multiplexer) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.world = world;
        this.match = match;
        this.p2IsImpostor = match.p1.equals(match.p2);
        this.music = assetManager.get(GameModule.ARENA_BG);
        music.setVolume(0.5f);
        this.multiplexer = multiplexer;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void show() {
        for (Entity layer : entitiesFactory.backgroundLayers()) {
            engine.addEntity(layer);
        }
        engine.addEntity(entitiesFactory.base());
        engine.addEntity(entitiesFactory.player(match.p1, Controls.Setup.P1, Player.P1));
        engine.addEntity(entitiesFactory.player(match.p2, Controls.Setup.P2, Player.P2, p2IsImpostor));
        music.play();
        multiplexer.addProcessor(muteWithM);
    }

    @Override
    public void hide() {
        music.stop();
        engine.removeAllEntities();
        engine.removeAllSystems();
        multiplexer.removeProcessor(muteWithM);
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }

    private final InputProcessor muteWithM = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.M) {
                if (music.isPlaying()) {
                    music.stop();
                } else {
                    music.play();
                }
                return true;
            }
            return super.keyDown(keycode);
        }
    };
}
