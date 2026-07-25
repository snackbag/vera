package net.snackbag.mcvera.test;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.layout.VVLayout;
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

        VScrollBox box = new VScrollBox(this, new VVLayout(this, 0, 0), 20, 20, 100, 100).alsoAdd();
        box.deltaYPerScroll = 3;

        VLabel label = new VLabel(box, "Hi there :)").alsoAdd();
        label.modifyStyleFontColor("font", VEffectState.HOVERED).rgb(VColor.MC_GOLD);

        for (int i = 0; i < 100; i++) {
            new VLabel(box, "num " + i).alsoAdd();
        }

        box.onScrolledY((event) -> box.animate(animation, true));
    }

    public VStyleSheet createStyleSheet() {
        VStyleSheet sheet = new VStyleSheet();

        sheet.setKey(VScrollBox.class, "border-color", VColor.MC_GREEN, VEffectState.HOVERED);
        sheet.setKey(VScrollBox.class, "overlay", VColor.MC_WHITE.withOpacity(0.15f), VEffectState.HOVERED);
        sheet.setKey(VScrollBox.class, "transition", 100);

        return sheet;
    }
}
