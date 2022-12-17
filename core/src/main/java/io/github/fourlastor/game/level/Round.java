package io.github.fourlastor.game.level;

public enum Round {
    ROUND_1("round-1"),
    ROUND_2("round-2"),
    FINAL_ROUND("final-round");
    public final String fileName;

    Round(String fileName) {
        this.fileName = fileName;
    }
}
