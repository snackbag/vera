package net.snackbag.vera.event;

import net.snackbag.vera.core.VeraApp;

public class VShortcut {
    private final VeraApp app;
    private final String combination;
    private Runnable event;

    public VShortcut(VeraApp app, String combination, Runnable event) {
        this.app = app;
        this.combination = combination.toLowerCase().replace(" ", "");
        this.event = event;
    }

    public VeraApp getApp() {
        return app;
    }

    public String getCombination() {
        return combination;
    }

    public Runnable getEvent() {
        return event;
    }

    public void setEvent(Runnable event) {
        this.event = event;
    }

    public void run() {
        app.getProvider().handleShortcut(this);
    }
}
