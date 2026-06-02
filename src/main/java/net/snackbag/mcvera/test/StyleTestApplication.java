package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.style.VInteractionState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.style.animation.VLoopMode;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VRect;

public class StyleTestApplication extends VeraApp {
    public static StyleTestApplication INSTANCE = new StyleTestApplication();

    private final VAnimation longTestAnimation = new VAnimation.Builder("long_test")
            .loopMode(VLoopMode.FORWARD_REPEAT)
            .unwindTime(1000)

            .keyframe(1000, 2000, frame -> frame.style("background", VColor.MC_RED))
            .keyframe(1000, 5000, frame -> frame.style("background", VColor.MC_GOLD))
            .keyframe(1000, 1000, frame -> frame.style("background", VColor.MC_WHITE))
            .build();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);

        mergeStyleSheet(createStyleSheet());

        // Animations
        VRect testRect = new VRect(VColor.black(), this).alsoAdd();
        testRect.move(10);

        testRect.onLeftClick(() -> testRect.animations.startOrRewind(longTestAnimation));
        testRect.onRightClick(() -> testRect.animations.unwind(longTestAnimation));

        testRect.setStyle("transition", 100);
        testRect.setStyle("background", VInteractionState.HOVERED, VColor.white());
        testRect.setStyle("background", VInteractionState.CLICKED, VColor.MC_RED);

        // Moving & classes
        VLabel testLabel = new VLabel("hello there", 40, 10, this)
                .alsoAddClass("label")
                .alsoAdd();
        testLabel.setStyle("scale", VInteractionState.DEFAULT, 1.0f);
        testLabel.setStyle("scale", VInteractionState.HOVERED, 1.2f);
        testLabel.setStyle("scale", VInteractionState.CLICKED, 2.0f);
        testLabel.setStyle("transition", 100);

        testLabel.onMouseDrag((ctx) -> testLabel.move(
                testLabel.getX() + ctx.moveX(),
                testLabel.getY() + ctx.moveY()
        ));
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey("label", "font", VFont.create().withColor(VColor.MC_GOLD.sub(80)));
        sheet.setKey("label", "font", VFont.create().withColor(VColor.MC_GOLD), VInteractionState.HOVERED);
        sheet.setKey("label", "cursor", VCursorShape.POINTING_HAND);

        return sheet;
    }
}
