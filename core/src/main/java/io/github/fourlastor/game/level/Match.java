package io.github.fourlastor.game.level;

public class Match {
    public final Round round;
    public final String p1;
    public final String p2;
    public final Player previousLoser;

    public Match(Round round, String p1, String p2, Player previousLoser) {
        this.round = round;
        this.p1 = p1;
        this.p2 = p2;
        this.previousLoser = previousLoser;
    }
}
