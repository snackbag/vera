package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VMouseButton;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.layout.VHLayout;
import net.snackbag.vera.layout.VVLayout;
import net.snackbag.vera.layout.VXLayout;
import net.snackbag.vera.style.VEffectState;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VScrollBox;

public class ScrollTestApplication extends VeraApp {
    public static ScrollTestApplication INSTANCE = new ScrollTestApplication();

    private final VAnimation animation = new VAnimation.Builder("cool-animation")
            .keyframe(
                    0, 200,
                    (kf) -> kf.style("border-color", VColor.MC_RED)
            ).build();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);
        mergeStyleSheet(createStyleSheet());

        VScrollBox vertBox = new VScrollBox(this, new VVLayout(this, 0, 0), 20, 20, 100, 100).alsoAdd();
        vertBox.deltaYPerScroll = 3;

        VLabel label = new VLabel(vertBox, "Hi there :)").alsoAdd();
        label.modifyStyleFontColor("font", VEffectState.HOVERED).rgb(VColor.MC_GOLD);

        for (int i = 0; i < 100; i++) {
            new VLabel(vertBox, "num " + i).alsoAdd();
        }

        vertBox.onScrolledY((event) -> vertBox.animate(animation, true));

        // ----

        VScrollBox horzBox = new VScrollBox(this, new VHLayout(this, 0, 0), 160, 20, 100, 100).alsoAdd();
        horzBox.deltaHPerScroll = 3;

        for (int i = 0; i < 100; i++) {
            new VLabel(horzBox, "num " + i).alsoAdd();
        }

        horzBox.onScrolledX((event) -> horzBox.animate(animation, true));

        // ----

        VScrollBox xBox = new VScrollBox(this, new VXLayout(this, 0, 0), 300, 20, 100, 100).alsoAdd();
        xBox.deltaHPerScroll = 3;
        xBox.deltaYPerScroll = 3;

        for (int i = 0; i < 100; i++) {
            new VLabel(xBox, "num " + i, i * 16, i * 16).alsoAdd();
        }

        xBox.setStyle("cursor", VEffectState.MC_DRAGGING, VCursorShape.ALL_RESIZE);
        xBox.onMouseDrag((e) -> {
            if (e.button() != VMouseButton.MIDDLE) return;

            xBox.setScrollX(xBox.getScrollX() - e.moveX());
            xBox.setScrollY(xBox.getScrollY() - e.moveY());
        });
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey(VScrollBox.class, "border-color", VColor.MC_GREEN, VEffectState.HOVERED);
        sheet.setKey(VScrollBox.class, "overlay", VColor.MC_WHITE.withOpacity(0.15f), VEffectState.HOVERED);
        sheet.setKey(VScrollBox.class, "transition", 100);

        return sheet;
    }
}
