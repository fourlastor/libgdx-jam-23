package io.github.fourlastor.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import io.github.fourlastor.game.di.GameComponent;
import io.github.fourlastor.game.level.di.LevelComponent;
import io.github.fourlastor.game.route.Router;
import io.github.fourlastor.game.route.RouterModule;
import io.github.fourlastor.game.selection.CharacterSelectionComponent;

public class MyGdxGame extends Game implements Router {

    private final InputMultiplexer multiplexer;

    private final LevelComponent.Builder levelScreenFactory;
    private final CharacterSelectionComponent.Builder caracterSelectionFactory;

    private Screen pendingScreen = null;

    public MyGdxGame(
            InputMultiplexer multiplexer,
            LevelComponent.Builder levelScreenFactory,
            CharacterSelectionComponent.Builder caracterSelectionFactory
    ) {
        this.multiplexer = multiplexer;
        this.levelScreenFactory = levelScreenFactory;
        this.caracterSelectionFactory = caracterSelectionFactory;
    }

    @Override
    public void create() {
//        if (Gdx.app.getType() != Application.ApplicationType.Android) {
//            Cursor customCursor =
//                    Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/included/cursor.png")), 0, 0);
//            Gdx.graphics.setCursor(customCursor);
//        }
        Gdx.input.setInputProcessor(multiplexer);
        goToCharacterSelection();
    }

    @Override
    public void render() {
        if (pendingScreen != null) {
            setScreen(pendingScreen);
            pendingScreen = null;
        }
        super.render();
    }

    public static MyGdxGame createGame() {
        return GameComponent.component().game();
    }


    @Override
    public void goToCharacterSelection() {
        pendingScreen = caracterSelectionFactory.router(new RouterModule(this)).build().screen();
    }

    @Override
    public void goToLevel() {
        pendingScreen =
                levelScreenFactory.router(new RouterModule(this)).build().screen();
    }
}
