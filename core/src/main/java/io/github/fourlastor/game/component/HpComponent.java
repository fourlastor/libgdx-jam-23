package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;

public class HpComponent implements Component {
    public final int maxHp = 100;
    public int hp = 100;

    public HpComponent() {
    }
}
