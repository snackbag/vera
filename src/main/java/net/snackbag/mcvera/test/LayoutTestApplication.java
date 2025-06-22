package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VLabel;

public class LayoutTestApplication extends VeraApp {
    public static final LayoutTestApplication INSTANCE = new LayoutTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide).alsoAdd();

        /*
        VLayout layout = new VVLayout(this);
        VLabel label = new VLabel("I'm a test", this).alsoAddTo(layout);

        VLayout secondLayout = new VHLayout(this);
        VLabel horiz1 = new VLabel("1", this).alsoAddTo(secondLayout);
        VLabel horiz2 = new VLabel("2", this).alsoAddTo(secondLayout);
         */
    }
}
