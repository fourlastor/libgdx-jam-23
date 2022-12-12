package io.github.fourlastor.game.animation.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

public class AnimatedGroup extends Group implements Animated {

    @Override
    public void enter(String name) {
        SnapshotArray<Actor> children = getChildren();
        Actor[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            Actor actor = actors[i];
            if (actor instanceof Animated) {
                ((Animated) actor).enter(name);
            }
        }
        children.end();
    }
}
