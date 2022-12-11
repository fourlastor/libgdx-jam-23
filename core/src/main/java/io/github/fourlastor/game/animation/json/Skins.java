package io.github.fourlastor.game.animation.json;

import java.util.Map;

public class Skins {
    public final String name;
    public final Map<String, Skin> skins;

    public Skins(String name, Map<String, Skin> skins) {
        this.name = name;
        this.skins = skins;
    }

    @Override
    public String toString() {
        return "Skins{" +
                "name='" + name + '\'' +
                ", skins=" + skins +
                '}';
    }
}
