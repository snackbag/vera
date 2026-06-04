package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.layout.VXLayout;
import net.snackbag.vera.style.VEffectState;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VScrollBox;

public class ScrollTestApplication extends VeraApp {
    public static ScrollTestApplication INSTANCE = new ScrollTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);

        VScrollBox box = new VScrollBox(20, 20, 100, 100, new VXLayout(this, 0, 0), this).alsoAdd();
        box.setStyle("border-size", 1);
        box.setStyle("border-color", VEffectState.HOVERED, VColor.MC_GREEN);
        box.setStyle("overlay", VEffectState.HOVERED, VColor.MC_WHITE.withOpacity(0.15f));
        box.setStyle("transition", 100);

        new VLabel("Hi there :)", box).alsoAdd();
    }
}
