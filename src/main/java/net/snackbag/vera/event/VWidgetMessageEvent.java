package net.snackbag.vera.event;

import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface VWidgetMessageEvent {
    void run(Context ctx);

    class Context {
        public final @NotNull VWidget<?> sender;
        public final @NotNull String type;
        public final @Nullable Object content;

        public Context(@NotNull VWidget<?> sender, @NotNull String type, @Nullable Object content) {
            this.sender = sender;
            this.type = type;
            this.content = content;
        }

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
