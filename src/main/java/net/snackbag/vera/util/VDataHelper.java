package net.snackbag.vera.util;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.function.Predicate;

public class VDataHelper {
    /**
     * Returns the sole element of an array if the supplied value is an array
     * containing exactly one element. All other values, including arrays with
     * zero or multiple elements, are returned unchanged.
     *
     * @param value the value to examine
     * @return the array's only element if it is a single-element array;
     * otherwise {@code value} itself
     */
    public static Object unwrapSingleElementArray(Object value) {
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            if (length == 1) value = Array.get(value, 0);
        }

        return value;
    }

    /**
     * Returns the first non-{@code null} value that satisfies the given predicate.
     *
     * <p>The values are evaluated in the order provided. {@code null} values are
     * skipped. If no value matches, {@code null} is returned.</p>
     *
     * @param evaluator the predicate used to test each value
     * @param values    the values to evaluate
     * @return the first matching value
     */
    @SafeVarargs
    public static <T> @Nullable T firstOf(Predicate<? super T> evaluator, T... values) {
        for (T v : values) {
            if (v == null) continue;
            if (evaluator.test(v)) return v;
        }

        return null;
    }
}
