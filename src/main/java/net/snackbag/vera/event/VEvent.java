package net.snackbag.vera.event;

@FunctionalInterface
public interface VEvent {
    void run(Object... args);
}
