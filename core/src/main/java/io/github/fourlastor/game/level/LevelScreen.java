package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.level.input.controls.Controls;

import javax.inject.Inject;
import javax.inject.Named;

public class LevelScreen extends ScreenAdapter {

    private static final float UI_WIDTH = 320f;
    private static final float UI_HEIGHT = 180f;
    private static final float HP_WIDTH = 130f;
    private static final float HP_HEIGHT = 20f;
    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;

    private final World world;
    private final String p1;
    private final String p2;
    private final boolean p2IsImpostor;

    private final Stage stage = new Stage(new FitViewport(UI_WIDTH, UI_HEIGHT));
    private final TextureAtlas atlas;

    @Inject
    public LevelScreen(
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            World world,
            @Named("P1") String p1,
            @Named("P2") String p2,
            TextureAtlas atlas) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.world = world;
        this.p1 = p1;
        this.p2 = p2;
        this.atlas = atlas;
        this.p2IsImpostor = p1.equals(p2);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        HpBars hpBars = setupHpBars();
        engine.addEntity(entitiesFactory.background());
        engine.addEntity(entitiesFactory.base());
        engine.addEntity(entitiesFactory.player(p1, Controls.Setup.P1, hpBars.p1, false));
        engine.addEntity(entitiesFactory.player(p2, Controls.Setup.P2, hpBars.p2, true, p2IsImpostor));
    }

    private static class HpBars {
        private final Image p1;
        private final Image p2;

        private HpBars(Image p1, Image p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    private HpBars setupHpBars() {
        Image koImage = new Image(atlas.findRegion("health-bar/ko"));
        koImage.setPosition(UI_WIDTH / 2 - koImage.getWidth() / 2, UI_HEIGHT - koImage.getHeight());
        stage.addActor(koImage);
        Group p1BarContainer = new Group();
        Image p1BarBg = new Image(atlas.findRegion("health-bar/red bar"));
        Image p1Bar = new Image(atlas.findRegion("health-bar/yellow bar"));
        p1BarBg.setSize(HP_WIDTH, HP_HEIGHT);
        p1Bar.setSize(HP_WIDTH, HP_HEIGHT);
        p1Bar.setOrigin(Align.right);
        p1BarContainer.addActor(p1BarBg);
        p1BarContainer.addActor(p1Bar);
        p1BarContainer.setPosition(
                UI_WIDTH / 2 - p1BarBg.getWidth() - koImage.getWidth() / 2,
                UI_HEIGHT - p1BarBg.getHeight() - 6
        );
        stage.addActor(p1BarContainer);
        Group p2BarContainer = new Group();
        Image p2BarBg = new Image(atlas.findRegion("health-bar/red bar"));
        Image p2Bar = new Image(atlas.findRegion("health-bar/yellow bar"));
        p2BarBg.setSize(HP_WIDTH, HP_HEIGHT);
        p2Bar.setSize(HP_WIDTH, HP_HEIGHT);
        p2BarContainer.addActor(p2BarBg);
        p2BarContainer.addActor(p2Bar);
        p2BarContainer.setPosition(
                UI_WIDTH / 2 + koImage.getWidth() / 2,
                UI_HEIGHT - p2BarBg.getHeight() - 6
        );
        stage.addActor(p2BarContainer);

        return new HpBars(p1Bar, p2Bar);
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
        stage.dispose();
    }
}
