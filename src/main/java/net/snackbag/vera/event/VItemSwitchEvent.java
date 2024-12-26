package net.snackbag.vera.event;

@FunctionalInterface
public interface VItemSwitchEvent {
    void run(int index);
}
