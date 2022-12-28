package io.github.fourlastor.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HpBarComponent implements Component {
    public final Image bar;

    public HpBarComponent(Image bar) {
        this.bar = bar;
    }
}
