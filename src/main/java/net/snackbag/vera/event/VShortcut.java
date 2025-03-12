package net.snackbag.vera.event;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import org.apache.commons.lang3.SystemUtils;

public class VShortcut {
    private final VeraApp app;
    private final String combination;
    private final boolean transformOSX;
    private Runnable event;

    public VShortcut(VeraApp app, String combination, Runnable event) {
        this(app, combination, event, true);
    }

    public VShortcut(VeraApp app, String combination, Runnable event, boolean transformOSX) {
        this.app = app;
        this.combination = combination.toLowerCase().replace(" ", "");
        this.event = event;
        this.transformOSX = transformOSX;
    }

    public VeraApp getApp() {
        return app;
    }

    public String getCombination() {
        return applyTransformations(transformOSX, combination);
    }

    public static String applyTransformations(boolean transformOSX, String combination) {
        return transformOSX && SystemUtils.IS_OS_MAC_OSX ?
                combination
                        .replace("leftctrl", "leftmeta")
                        .replace("rightctrl", "rightmeta")
                : combination;
    }

    public Runnable getEvent() {
        return event;
    }

    public void setEvent(Runnable event) {
        this.event = event;
    }

    public boolean shouldTransformOSX() {
        return transformOSX;
    }

    public void run() {
        Vera.provider.handleRunShortcut(this);
    }

    public VShortcut alsoAdd() {
        app.addShortcut(this);
        return this;
    }
}
