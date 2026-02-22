package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.style.animation.VLoopMode;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VRect;

public class StyleTestApplication extends VeraApp {
    public static StyleTestApplication INSTANCE = new StyleTestApplication();

    private final VAnimation testAnimation = new VAnimation.Builder("test")
            .keyframe(1000, 5000, frame -> {
                frame.style("background-color", VColor.MC_RED);
            })
            .build();
    private final VAnimation longTestAnimation = new VAnimation.Builder("long_test")
            .loopMode(VLoopMode.FORWARD_REPEAT)
            .keyframe(1000, 2000, frame -> {
                frame.style("background-color", VColor.MC_RED);
            })
            .keyframe(1000, 5000, frame -> {
                frame.style("background-color", VColor.MC_GOLD);
            })
            .keyframe(1000, 1000, frame -> {
                frame.style("background-color", VColor.MC_WHITE);
            })
            .build();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);

        mergeStyleSheet(createStyleSheet());

        new VLabel("helo", this)
                .alsoAddClass("label")
                .alsoAdd();

        VRect testRect = new VRect(VColor.black(), this).alsoAdd();
        testRect.onLeftClick(() -> {
            testRect.animations.start(longTestAnimation);
        });

        testRect.onRightClick(() -> {
            testRect.animations.stop(longTestAnimation);
        });
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey("label", "font", VFont.create());
        sheet.setKey("label", "font", VFont.create().withColor(VColor.MC_GOLD), StyleState.HOVERED);

        return sheet;
    }
}
