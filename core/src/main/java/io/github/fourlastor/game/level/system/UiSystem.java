package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.fourlastor.game.component.AnimationFinishedComponent;
import io.github.fourlastor.game.component.HpBarComponent;
import io.github.fourlastor.game.component.HpChangedComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.Match;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.route.Router;

import javax.inject.Inject;

public class UiSystem extends EntitySystem implements Telegraph {

    private static final Family HP_CHANGED = Family.all(HpComponent.class, HpBarComponent.class, HpChangedComponent.class).get();
    private static final Family HP_SETUP = Family.all(HpComponent.class, PlayerComponent.class).get();
    private static final float UI_WIDTH = 320f;
    private static final float UI_HEIGHT = 180f;
    private static final float HP_WIDTH = 130f;
    private static final float HP_HEIGHT = 20f;
    private final Stage stage = new Stage(new FitViewport(UI_WIDTH, UI_HEIGHT));

    private final TextureAtlas atlas;
    private final Match match;
    private final MessageDispatcher dispatcher;
    private final Router router;
    private Image koImage;

    @Inject
    public UiSystem(
            TextureAtlas atlas,
            Match match,
            MessageDispatcher dispatcher, Router router) {
        this.atlas = atlas;
        this.match = match;
        this.dispatcher = dispatcher;
        this.router = router;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(HP_SETUP, hpBarSetupListener);
        engine.addEntityListener(HP_CHANGED, hpChangedListener);
        koImage = new Image(atlas.findRegion("health-bar/ko"));
        koImage.setPosition(UI_WIDTH / 2 - koImage.getWidth() / 2, UI_HEIGHT - koImage.getHeight());
        stage.addActor(koImage);
        Image overlayImage = new Image(atlas.findRegion("text-overlays/" + match.round.fileName));
        overlayImage.setOrigin(Align.center);
        overlayImage.setPosition(UI_WIDTH / 2 - overlayImage.getWidth() / 2, UI_HEIGHT / 2 - overlayImage.getHeight() / 2);
        overlayImage.addAction(Actions.sequence(
                Actions.delay(0.7f),
                Actions.scaleTo(0.01f, 0.01f, 0.3f),
                Actions.run(() -> {
                    TextureAtlas.AtlasRegion region = atlas.findRegion("text-overlays/fight");
                    overlayImage.setDrawable(new TextureRegionDrawable(region));
                    overlayImage.setSize(region.originalWidth, region.originalHeight);
                    overlayImage.setOrigin(Align.center);
                    overlayImage.setPosition(UI_WIDTH / 2 - overlayImage.getWidth() / 2, UI_HEIGHT / 2 - overlayImage.getHeight() / 2);
                }),
                Actions.scaleTo(1f, 1f, 0.7f, Interpolation.bounceOut),
                Actions.delay(0.3f),
                Actions.run(() -> {
                    Entity entity = new Entity();
                    entity.add(new AnimationFinishedComponent());
                    engine.addEntity(entity);
                    overlayImage.remove();
                })
        ));
        stage.addActor(overlayImage);
        dispatcher.addListener(this, Message.MATCH_END.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(hpBarSetupListener);
        dispatcher.removeListener(this);
        stage.dispose();
        super.removedFromEngine(engine);
    }

    private final EntityListener hpChangedListener = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
            entity.remove(HpChangedComponent.class);
            Image bar = entity.getComponent(HpBarComponent.class).bar;
            HpComponent hpComponent = entity.getComponent(HpComponent.class);
            bar.clearActions();
            bar.addAction(Actions.scaleTo(
                    (float) hpComponent.hp / hpComponent.maxHp,
                    1f,
                    0.5f,
                    Interpolation.bounce
            ));
        }

        @Override
        public void entityRemoved(Entity entity) {

        }
    };

    private final EntityListener hpBarSetupListener = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
            Group barContainer = new Group();
            Image barBg = new Image(atlas.findRegion("health-bar/red bar"));
            Image bar = new Image(atlas.findRegion("health-bar/yellow bar"));
            barBg.setSize(HP_WIDTH, HP_HEIGHT);
            bar.setSize(HP_WIDTH, HP_HEIGHT);
            barContainer.addActor(barBg);
            barContainer.addActor(bar);

            if (entity.getComponent(PlayerComponent.class).player.flipped) {
                barContainer.setPosition(
                        UI_WIDTH / 2 + koImage.getWidth() / 2,
                        UI_HEIGHT - barBg.getHeight() - 6
                );
            } else {
                bar.setOrigin(Align.right);
                barContainer.setPosition(
                        UI_WIDTH / 2 - barBg.getWidth() - koImage.getWidth() / 2,
                        UI_HEIGHT - barBg.getHeight() - 6
                );
            }
            entity.add(new HpBarComponent(bar));
            stage.addActor(barContainer);
        }

        @Override
        public void entityRemoved(Entity entity) {

        }
    };

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.MATCH_END.ordinal() && msg.extraInfo instanceof Player) {
            Player winner = ((Player) msg.extraInfo);
            String winnerName = winner == Player.P1 ? match.p1.charName : match.p2.charName;
            Image overlayImage = new Image(atlas.findRegion("text-overlays/" + winnerName + "-won"));
            overlayImage.setOrigin(Align.center);
            overlayImage.setPosition(UI_WIDTH / 2 - overlayImage.getWidth() / 2, UI_HEIGHT / 2 - overlayImage.getHeight() / 2);
            stage.addActor(overlayImage);
            stage.addAction(Actions.sequence(
                    Actions.delay(4f),
                    Actions.run(() -> router.goToCharacterSelection())
            ));
            return true;
        }
        return false;
    }
}
