package io.github.fourlastor.game.level;

import io.github.fourlastor.game.Fighter;

public class Match {
    public final Round round;
    public final Fighter p1;
    public final Fighter p2;
    public final Player previousLoser;

    public Match(Round round, Fighter p1, Fighter p2, Player previousLoser) {
        this.round = round;
        this.p1 = p1;
        this.p2 = p2;
        this.previousLoser = previousLoser;
    }
}
