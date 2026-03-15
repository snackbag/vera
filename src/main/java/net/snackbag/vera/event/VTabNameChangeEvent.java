package net.snackbag.vera.event;

@FunctionalInterface
public interface VTabNameChangeEvent {
    void run(String name);
}
