package io.github.fourlastor.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import io.github.fourlastor.game.di.GameComponent;
import io.github.fourlastor.game.level.Match;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.level.Round;
import io.github.fourlastor.game.level.di.LevelComponent;
import io.github.fourlastor.game.level.di.LevelModule;
import io.github.fourlastor.game.route.Router;
import io.github.fourlastor.game.route.RouterModule;
import io.github.fourlastor.game.selection.CharacterSelectionComponent;

public class MyGdxGame extends Game implements Router {

    public static final Color IMPOSTOR_COLOR = new Color(0.7f, 1f, 0.8f, 1f);
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
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
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
    public void goToLevel(String p1, String p2, Round round, Player previousLoser) {

        pendingScreen =
                levelScreenFactory
                        .router(new RouterModule(this))
                        .level(new LevelModule(new Match(round, Fighter.fighter(p1), Fighter.fighter(p2), previousLoser)))
                        .build()
                        .screen();
    }
}
