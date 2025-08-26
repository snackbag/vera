package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;

public class LayoutCenteringTestApplication extends VeraApp {
    public static LayoutCenteringTestApplication INSTANCE = new LayoutCenteringTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);


    }
}
