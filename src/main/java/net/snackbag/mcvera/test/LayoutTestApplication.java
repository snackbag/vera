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
        VLabel label = new VLabel("I'm a test", this).alsoAdd(); layout.addElement(label);
        VLabel label2 = new VLabel("I'm another test", this).alsoAdd(); layout.addElement(label2);

        VLayout secondLayout = new VHLayout(this, 0, 0);
        VLabel horiz1 = new VLabel("1", this).alsoAdd(); secondLayout.addElement(horiz1);
        VLabel horiz2 = new VLabel("2", this).alsoAdd(); secondLayout.addElement(horiz2);

        VLayout thirdLayout = new VVLayout(this, 0, 0);
        VLabel label3 = new VLabel("oh?", this).alsoAdd(); thirdLayout.addElement(label3);
        VLabel label4 = new VLabel("oh!!!!", this).alsoAdd(); thirdLayout.addElement(label4);

        secondLayout.addElement(thirdLayout);
        layout.addElement(secondLayout);
    }
}
