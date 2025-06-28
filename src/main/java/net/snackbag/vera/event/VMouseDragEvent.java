package net.snackbag.vera.event;

public interface VMouseDragEvent {
    void run(Context ctx);

    record Context(int startX, int startY, int currentX, int currentY, int moveX, int moveY, Direction direction) {
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
