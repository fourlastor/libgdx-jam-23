package io.github.fourlastor.game.animation.scene2d;

import io.github.fourlastor.game.animation.json.KeyFrame;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class AnimatedValue<T> {

    private static final Comparator<KeyFrame<?>> COMPARATOR = Comparator.comparingInt(new KeyExtractor());

    private final List<KeyFrame<T>> values;

    public AnimatedValue(List<KeyFrame<T>> values) {
        values.sort(COMPARATOR);
        this.values = values;
    }


    private final KeyFrame<T> searchFrame = new KeyFrame<>(0, null);

    public int findIndex(int position) {
        searchFrame.start = position;
        int result = Collections.binarySearch(values, searchFrame, COMPARATOR);
        return unwrapInsertionPoint(result);
    }

    public KeyFrame<T> get(int index) {
        return values.get(index);
    }

    /**
     * Transforms the result of [{@link Collections#binarySearch} in case the element isn't found.
     *
     * @return [result] if an element was found (result >= 0), the first index before the "insertion point" otherwise.
     */
    private int unwrapInsertionPoint(int result) {
        return result < 0 ? -result - 2 : result;
    }

    private static class KeyExtractor implements ToIntFunction<KeyFrame<?>> {

        @Override
        public int applyAsInt(KeyFrame value) {
            return value.start;
        }
    }
}
