package io.github.fourlastor.game.level.input.controls;

import com.badlogic.gdx.Input;

public interface Controls {
    Control left();

    Control right();

    Control attack();

    enum Setup implements Controls {


        P1(Input.Keys.A, Input.Keys.D, Input.Keys.F),
        P2(Input.Keys.J, Input.Keys.L, Input.Keys.H);

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
