package io.github.fourlastor.game.level;

public enum Player {
    P1(false), P2(true);

    public final boolean flipped;

    Player(boolean flipped) {

        this.flipped = flipped;
    }
}
