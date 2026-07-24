package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.layout.VXLayout;
import net.snackbag.vera.style.VEffectState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VScrollBox;

public class ScrollTestApplication extends VeraApp {
    public static ScrollTestApplication INSTANCE = new ScrollTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);
        mergeStyleSheet(createStyleSheet());

        VScrollBox box = new VScrollBox(this, new VXLayout(this, 0, 0), 20, 20, 100, 100).alsoAdd();
        box.deltaYPerScroll = 3;

        VLabel label = new VLabel(box, "Hi there :)").alsoAdd();
        label.modifyStyleFontColor("font", VEffectState.HOVERED).rgb(VColor.MC_GOLD);
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey(VScrollBox.class, "border-color", VColor.MC_GREEN, VEffectState.HOVERED);
        sheet.setKey(VScrollBox.class, "overlay", VColor.MC_WHITE.withOpacity(0.15f), VEffectState.HOVERED);
        sheet.setKey(VScrollBox.class, "transition", 100);

        return sheet;
    }
}
