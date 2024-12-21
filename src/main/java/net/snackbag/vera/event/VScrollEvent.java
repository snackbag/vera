package net.snackbag.vera.event;

@FunctionalInterface
public interface VScrollEvent {
    void run(int x, int y, double amount);
}
