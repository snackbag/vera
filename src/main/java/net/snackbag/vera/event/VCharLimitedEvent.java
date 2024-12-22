package net.snackbag.vera.event;

@FunctionalInterface
public interface VCharLimitedEvent {
    void run(char chr);
}
