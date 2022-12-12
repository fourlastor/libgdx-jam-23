package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.animation.EntityJsonParser;
import io.github.fourlastor.game.animation.json.EntityData;

import javax.inject.Inject;

public class LevelScreen extends ScreenAdapter {

    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;

    private final World world;
    private final EntityJsonParser parser;

    @Inject
    public LevelScreen(
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            World world,
            EntityJsonParser parser
    ) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.world = world;
        this.parser = parser;
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
        EntityData entityData = parser.parse(Gdx.files.internal("entities/kick_test.json"));
        System.out.println(entityData);
        engine.addEntity(entitiesFactory.base());
        engine.addEntity(entitiesFactory.player());
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
