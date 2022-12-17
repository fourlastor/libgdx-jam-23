package io.github.fourlastor.game.route;

import io.github.fourlastor.game.level.Round;

public interface Router {

    void goToCharacterSelection();

    void goToLevel(String p1, String p2, Round round);
}
