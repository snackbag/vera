package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VRect;

public class StyleTestApplication extends VeraApp {
    public static StyleTestApplication INSTANCE = new StyleTestApplication();

    private final VAnimation testAnimation = new VAnimation.Builder(this, "test")
            .unwindTime(2000)

            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_GOLD), 2000)
            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_RED), 2000)
            .build();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide).alsoAdd();

        mergeStyleSheet(createStyleSheet());

        new VLabel("helo", this)
                .alsoAddClass("label")
                .alsoAdd();

        VRect testRect = new VRect(VColor.black(), this).alsoAdd();
        testRect.setStyle("background-color", StyleState.HOVERED, VColor.white());

        testRect.setStyle("cursor", StyleState.HOVERED, VCursorShape.POINTING_HAND);
        testRect.setStyle("cursor", StyleState.CLICKED, VCursorShape.ALL_RESIZE);

        testRect.onMouseDragLeft((ctx) -> testRect.move(testRect.getX() + ctx.moveX(), testRect.getY() + ctx.moveY()));

        new VShortcut(this, "a", () -> {
            testRect.animations.activate(
                    new VAnimation.Builder(this, "test")
                            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_GOLD), 2000)
                            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_RED), 2000)
                            .build()
            );
        }).alsoAdd();
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey("label", "font", VFont.create());
        sheet.setKey("label", "font", VFont.create().withColor(VColor.MC_GOLD), StyleState.HOVERED);

        return sheet;
    }
}
