package io.github.fourlastor.game;

public enum Fighter {
    NISSEFAR("nissefar", 450, 1200, 1000, 15),
    LANGNISSE("langnisse", 600, 1000, 800, 20),
    NISSEMOR("nissemor", 300, 900, 700, 10),
    ;

    public final String charName;
    public final int attack;
    public final int idle;
    public final int walking;
    public final int damage;

    Fighter(String charName, int attack, int idle, int walking, int damage) {
        this.charName = charName;
        this.attack = attack;
        this.idle = idle;
        this.walking = walking;
        this.damage = damage;
    }

    public static Fighter fighter(String name) {
        for (Fighter fighter : Fighter.values()) {
            if (name.equals(fighter.charName)) {
                return fighter;
            }
        }
        throw new IllegalStateException("No fighter found!");
    }
}
