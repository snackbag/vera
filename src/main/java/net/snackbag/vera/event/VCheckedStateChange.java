package net.snackbag.vera.event;

@FunctionalInterface
public interface VCheckedStateChange {
    void run(boolean checked);
}
