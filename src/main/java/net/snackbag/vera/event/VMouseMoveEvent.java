package net.snackbag.vera.event;

@FunctionalInterface
public interface VMouseMoveEvent {
    void run(int x, int y);
}
