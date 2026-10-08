package net.snackbag.vera.widget;

import net.snackbag.vera.event.EventHandler;

public interface VDelegator {
    EventHandler getDelegatedEventHandler();

    boolean isDelegatedPointOver(int px, int py, VWidget<?> widget);
}
