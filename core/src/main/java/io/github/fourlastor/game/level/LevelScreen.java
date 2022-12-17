package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.level.input.controls.Controls;

import javax.inject.Inject;
import javax.inject.Named;

public class LevelScreen extends ScreenAdapter {

    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;

    private final World world;
    private final String p1;
    private final String p2;
    private final boolean p2IsImpostor;

    @Inject
    public LevelScreen(
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            World world,
            @Named("P1") String p1,
            @Named("P2") String p2
    ) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.world = world;
        this.p1 = p1;
        this.p2 = p2;
        this.p2IsImpostor = p1.equals(p2);
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
        engine.addEntity(entitiesFactory.background());
        engine.addEntity(entitiesFactory.base());
        engine.addEntity(entitiesFactory.player(p1, Controls.Setup.P1, false));
        engine.addEntity(entitiesFactory.player(p2, Controls.Setup.P2, true, p2IsImpostor));
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
