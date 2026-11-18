package net.snackbag.mcvera.test;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.flag.VLayoutAlignmentFlag;
import net.snackbag.vera.layout.VHLayout;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.layout.VVLayout;
import net.snackbag.vera.widget.VLabel;

public class LayoutAlignmentTestApplication extends VeraApp {
    public static LayoutAlignmentTestApplication INSTANCE = new LayoutAlignmentTestApplication();

    private VLayout left;
    private VLayout center;
    private VLayout right;

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);

        left = new VHLayout(this, 0, 0);
        left.alignment = VLayoutAlignmentFlag.START;
        new VLabel(this, "left").alsoAddTo(left);

        center = new VHLayout(this, 0, 0);
        center.alignment = VLayoutAlignmentFlag.CENTER;
        new VLabel(this, "center").alsoAddTo(center);

        right = new VHLayout(this, 0, 0);
        right.alignment = VLayoutAlignmentFlag.END;
        new VLabel(this, "right").alsoAddTo(right);
    }

    @Override
    public void update() {
        setSize(Vera.getScreenWidth(), Vera.getScreenHeight());

        left.setSize(getWidth(), 100);
        center.setSize(getWidth(), 100);
        right.setSize(getWidth(), 100);
    }
}
