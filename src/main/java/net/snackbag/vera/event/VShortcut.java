package net.snackbag.vera.event;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.ApiStatus;

public class VShortcut {
    public final VeraApp app;
    private final String combination;
    public final boolean transformOSX;
    private Runnable event;

    public VShortcut(VeraApp app, String combination, Runnable event) {
        this(app, combination, event, true);
    }

    public VShortcut(VeraApp app, String combination, Runnable event, boolean transformOSX) {
        this.app = app;
        this.combination = combination.toLowerCase().replace(" ", "");
        this.event = event;
        this.transformOSX = transformOSX;

        this.app.addShortcut(this);
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

    public void run() {
        Vera.provider.handleRunShortcut(this);
    }

    /**
     * Deprecated since version 2.0, will be removed in 2.1. No longer needed since the app now already receives the
     * shortcut on shortcut initialization.
     */
    @Deprecated(forRemoval = true, since = "2.0")
    @ApiStatus.ScheduledForRemoval(inVersion = "2.1")
    public VShortcut alsoAdd() {
        app.addShortcut(this);
        return this;
    }
}
