package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;

public class HurtData {
    public final Entity entity;
    public final int damage;

    public HurtData(Entity entity, int damage) {
        this.entity = entity;
        this.damage = damage;
    }
}
