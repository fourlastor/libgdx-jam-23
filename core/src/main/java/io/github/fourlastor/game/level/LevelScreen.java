package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.level.input.controls.Controls;

import javax.inject.Inject;

public class LevelScreen extends ScreenAdapter {

    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;

    private final World world;
    private final Match match;
    private final boolean p2IsImpostor;

    @Inject
    public LevelScreen(
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            World world,
            Match match) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.world = world;
        this.match = match;
        this.p2IsImpostor = match.p1.equals(match.p2);
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
    }

    @Override
    public void hide() {
        engine.removeAllEntities();
        engine.removeAllSystems();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }
}
