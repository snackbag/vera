package net.snackbag.vera.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Once<T> {
    @Nullable
    private T value;

    /**
     * Sets the value if not already set
     *
     * @param value the value to set
     * @throws UnsupportedOperationException if value is already set
     * @throws NullPointerException          if given value is null
     */
    public void set(@NotNull T value) {
        Objects.requireNonNull(value, "value must not be null");

        if (this.value != null) throw new UnsupportedOperationException("Cannot set value that is already set");
        this.value = value;
    }

    /**
     * Sets the value if not already set without throwing exceptions
     *
     * @param value the value to set
     * @return <code>true</code> if value could be set, <code>false</code> if unable to set
     */
    public boolean setSafe(T value) {
        if (value == null) return false;
        if (this.value != null) return false;

        this.value = value;
        return true;
    }

    /**
     * Gets the set value if not null
     *
     * @return the value
     * @throws NullPointerException if value is null
     */
    public @NotNull T get() {
        if (value == null) throw new NullPointerException("Cannot get value that isn't set");
        return value;
    }
}
