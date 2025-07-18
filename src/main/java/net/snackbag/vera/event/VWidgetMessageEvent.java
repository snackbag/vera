package net.snackbag.vera.event;

import net.snackbag.vera.VElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface VWidgetMessageEvent {
    void run(Context ctx);

    record Context(@NotNull VElement sender, @NotNull String type, @Nullable Object content) {
        public boolean isContentNull() {
                return content == null;
            }

            public boolean isContentString() {
                return content != null && content instanceof String;
            }

            public <T> T getContentOrDefault(T default_) {
                return content == null ? default_ : (T) content;
            }
        }
}
