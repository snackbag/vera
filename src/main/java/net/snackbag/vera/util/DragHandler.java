package net.snackbag.vera.util;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VMouseButton;
import net.snackbag.vera.event.VMouseDragEvent;
import net.snackbag.vera.widget.VWidget;
import org.joml.Vector2i;

public class DragHandler {
    public static VMouseButton button = null;
    public static VWidget<?> target = null;
    public static Vector2i beginPos = null;

    public static VMouseDragEvent.Context prevContext = null;

    public static boolean isDragging() {
        return button != null && target != null;
    }

    public static void down(VMouseButton button, VWidget<?> target) {
        if (isDragging()) return;

        DragHandler.button = button;
        DragHandler.target = target;
        DragHandler.beginPos = new Vector2i(Vera.getMouseX(), Vera.getMouseY());

        fireEvents();
    }

    public static void move() {
        if (!isDragging()) return;

        fireEvents();
    }

    public static void release(VMouseButton button) {
        if (!isDragging()) return;
        if (button != DragHandler.button) return;

        DragHandler.target = null;
        DragHandler.button = null;
        DragHandler.beginPos = null;
        DragHandler.prevContext = null;
    }

    public static VMouseDragEvent.Context createContext() {
        if (!isDragging()) throw new UnsupportedOperationException("Cannot create mouse drag context when not dragging");

        int x = Vera.getMouseX();
        int y = Vera.getMouseY();

        Vector2i move = createMove();

        return new VMouseDragEvent.Context(
                beginPos.x, beginPos.y,
                x, y,
                move.x, move.y,
                VMouseDragEvent.Direction.UP
        );
    }

    private static Vector2i createMove() {
        if (prevContext == null) return new Vector2i(0, 0);
        else return new Vector2i(
                Vera.getMouseX() - prevContext.currentX(),
                Vera.getMouseY() - prevContext.currentY()
        );
    }

    private static void fireEvents() {
        switch (button) {
            case LEFT -> target.events.fire("mouse-drag-left", createContext());
            case MIDDLE -> target.events.fire("mouse-drag-middle", createContext());
            case RIGHT -> target.events.fire("mouse-drag-right", createContext());
        }
    }
}
