package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HpComponent implements Component {
    public final Image bar;
    public final int maxHp = 100;
    public int hp = 100;
    public boolean hpChanged = false;

    public HpComponent(Image bar) {
        this.bar = bar;
    }
}
