package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.layout.VHLayout;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.layout.VVLayout;
import net.snackbag.vera.widget.VLabel;

public class LayoutTestApplication extends VeraApp {
    public static LayoutTestApplication INSTANCE = new LayoutTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);

        VLayout layout = new VVLayout(this, 0, 0);
        new VLabel(this, "I'm a test").alsoAddTo(layout);
        new VLabel(this, "I'm another test").alsoAddTo(layout);

        VLayout secondLayout = new VHLayout(layout);
        new VLabel(this, "1").alsoAddTo(secondLayout);
        new VLabel(this, "2").alsoAddTo(secondLayout);

        VLayout thirdLayout = new VVLayout(secondLayout);
        new VLabel(this, "oh?").alsoAddTo(thirdLayout);
        new VLabel(this, "oh!!!!").alsoAddTo(thirdLayout);
    }
}
