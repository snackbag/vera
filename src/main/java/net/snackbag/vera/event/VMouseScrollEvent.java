package net.snackbag.vera.event;

@FunctionalInterface
public interface VMouseScrollEvent {
    void run(int x, int y, double amount);
}
