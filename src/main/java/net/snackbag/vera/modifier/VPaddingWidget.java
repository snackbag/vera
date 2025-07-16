package net.snackbag.vera.modifier;

import net.snackbag.vera.core.V4Int;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.ScheduledForRemoval(inVersion = "1.11")
@Deprecated(forRemoval = true, since = "1.10")
public interface VPaddingWidget {
    V4Int getPadding();

    void setPadding(V4Int padding);

    default void setPadding(int all) {
        setPadding(new V4Int(all));
    }

    default void setPadding(int tb, int lr) {
        setPadding(new V4Int(tb, lr));
    }

    default void setPadding(int top, int bottom, int left, int right) {
        setPadding(new V4Int(top, bottom, left, right));
    }
}
