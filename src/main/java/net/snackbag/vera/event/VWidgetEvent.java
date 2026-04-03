package net.snackbag.vera.event;

import net.snackbag.vera.core.VMouseButton;

import java.nio.file.Path;
import java.util.List;

public class VWidgetEvent {
    public record MouseScroll(int x, int y, double amount) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Widget.SCROLL;
        }
    }

    public record MouseMove(int x, int y) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Widget.MOUSE_MOVE;
        }
    }

    public record MouseDrag(int startX, int startY, int currentX, int currentY, int moveX, int moveY, Direction direction, VMouseButton button) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Widget.MOUSE_DRAG;
        }

        public enum Direction {
            UP,
            DOWN,
            LEFT,
            RIGHT
        }
    }

    public record TransparencyStateChanged(boolean transparent) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Widget.TRANSPARENCY_STATE_CHANGED;
        }
    }

    public record FilesDropped(List<Path> paths) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Widget.FILES_DROPPED;
        }
    }
}
