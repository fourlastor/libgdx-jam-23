package io.github.fourlastor.game.level.input.controls;

import com.badlogic.gdx.Input;

public interface Controls {
    Control left();

    Control right();

    Control attack();

    enum Setup implements Controls {


        P1(Input.Keys.A, Input.Keys.D, Input.Keys.SPACE),
        P2(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER);

        private final KeysControl left;
        private final KeysControl right;
        private final KeysControl attack;

        Setup(int leftKey, int rightKey, int attackKey) {
            left = new KeysControl(leftKey);
            right = new KeysControl(rightKey);
            attack = new KeysControl(attackKey);
        }

        @Override
        public Control left() {
            return left;
        }

        @Override
        public Control right() {
            return right;
        }

        @Override
        public Control attack() {
            return attack;
        }
    }
}
