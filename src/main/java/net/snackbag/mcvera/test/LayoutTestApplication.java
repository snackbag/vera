package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.layout.VHLayout;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.layout.VVLayout;
import net.snackbag.vera.widget.VLabel;

public class LayoutTestApplication extends VeraApp {
    public static final LayoutTestApplication INSTANCE = new LayoutTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide).alsoAdd();

        VLayout layout = new VVLayout(this, 0, 0);
        new VLabel("I'm a test", this).alsoAddTo(layout);
        new VLabel("I'm another test", this).alsoAddTo(layout);

        VLayout secondLayout = new VHLayout(layout);
        new VLabel("1", this).alsoAddTo(secondLayout);
        new VLabel("2", this).alsoAddTo(secondLayout);

        VLayout thirdLayout = new VVLayout(secondLayout);
        new VLabel("oh?", this).alsoAddTo(thirdLayout);
        new VLabel("oh!!!!", this).alsoAddTo(thirdLayout);
    }
}
