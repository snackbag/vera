package net.snackbag.vera.event;

import net.snackbag.vera.VElement;
import net.snackbag.vera.layout.VLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VElementEvent {
    public record LayoutSwap(VLayout layout) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Element.LAYOUT_SWAP;
        }
    }

    public record Message(@NotNull VElement sender, @NotNull String type, @Nullable Object content) implements VEventContext {
        public boolean isContentNull() {
            return content == null;
        }

        public boolean isContentString() {
            return content != null && content instanceof String;
        }

        public <T> T getContentOrDefault(T default_) {
            return content == null ? default_ : (T) content;
        }

        @Override
        public String eventName() {
            return VEvents.Element.MESSAGE;
        }
    }
}
