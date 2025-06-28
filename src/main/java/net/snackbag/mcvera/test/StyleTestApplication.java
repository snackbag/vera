package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VRect;

public class StyleTestApplication extends VeraApp {
    public static final StyleTestApplication INSTANCE = new StyleTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide).alsoAdd();

        mergeStyleSheet(createStyleSheet());

        new VLabel("helo", this)
                .alsoAddClass("label")
                .alsoAdd();

        VRect testRect = new VRect(VColor.black(), this).alsoAdd();
        testRect.setStyle("color", StyleState.HOVERED, VColor.white());

        testRect.setStyle("cursor", StyleState.HOVERED, VCursorShape.POINTING_HAND);
        testRect.setStyle("cursor", StyleState.CLICKED, VCursorShape.ALL_RESIZE);

        testRect.onMouseDragLeft((ctx) -> testRect.move(testRect.getX() + ctx.moveX(), testRect.getY() + ctx.moveY()));
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey("label", "font", VFont.create());
        sheet.setKey("label", "font", VFont.create().withColor(VColor.MC_GOLD), StyleState.HOVERED);

        return sheet;
    }
}
