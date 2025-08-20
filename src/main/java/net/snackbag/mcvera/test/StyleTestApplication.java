package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.LoopMode;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VRect;

public class StyleTestApplication extends VeraApp {
    public static StyleTestApplication INSTANCE = new StyleTestApplication();

    private final VAnimation testAnimation = new VAnimation.Builder(this, "test")
            .unwindTime(2000)
            .unwindOnFinish()
            .loop(LoopMode.REPEAT)

            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_GOLD), 2000)
            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_RED), 2000)
            .build();

    private final VAnimation hoverAnimation = new VAnimation.Builder(this, "hover")
            .unwindTime(1000)
            .keepFinalStyle(StyleState.HOVERED)

            .keyframe(1000, frame -> frame.style("background-color", VColor.MC_WHITE), 0)
            .build();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide).alsoAdd();

        mergeStyleSheet(createStyleSheet());

        new VLabel("helo", this)
                .alsoAddClass("label")
                .alsoAdd();

        VRect testRect = new VRect(VColor.black(), this).alsoAdd();
        testRect.onHover(() -> testRect.animations.activateOrRewind(hoverAnimation));
        //testRect.onHoverLeave(() -> testRect.animations.unwindOrActivateReversed(hoverAnimation));

        testRect.setStyle("cursor", StyleState.HOVERED, VCursorShape.POINTING_HAND);
        testRect.setStyle("cursor", StyleState.CLICKED, VCursorShape.ALL_RESIZE);

        testRect.onMouseDragLeft((ctx) -> testRect.move(testRect.getX() + ctx.moveX(), testRect.getY() + ctx.moveY()));

        new VShortcut(this, "a", () -> testRect.animations.activate(testAnimation)).alsoAdd();
        new VShortcut(this, "u", () -> testRect.animations.unwind(testAnimation)).alsoAdd();
        new VShortcut(this, "r", () -> testRect.animations.rewind(testAnimation)).alsoAdd();
        new VShortcut(this, "k", () -> testRect.animations.kill(testAnimation)).alsoAdd();
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey("label", "font", VFont.create());
        sheet.setKey("label", "font", VFont.create().withColor(VColor.MC_GOLD), StyleState.HOVERED);

        return sheet;
    }
}
