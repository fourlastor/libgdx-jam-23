package io.github.fourlastor.game.level.physics;

public class Bits {

    public enum Category {
        BODY, HITBOX, HURTBOX;
        public final short bits;

        Category() {
            this.bits = (short) (1 << ordinal());
        }
    }

    public enum Mask {
        BODY(Category.BODY),
        HITBOX(Category.HURTBOX),
        HURTBOX(Category.HITBOX),
        DISABLED();

        public final short bits;

        Mask(Category... categories) {
            short bits = 0;
            for (Category category : categories) {
                bits |= category.bits;
            }
            this.bits = bits;
        }
    }
}
