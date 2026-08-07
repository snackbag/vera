package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.style.VEffectState;

public class VRect extends VWidget<VRect> {
    public VRect(VAppAccess app, VFill background) {
        this(app, background, 0, 0, 20, 20);
    }

    public VRect(VAppAccess app, VFill background, int x, int y) {
        this(app, background, x, y, 20, 20);
    }

    public VRect(VAppAccess app, VFill background, int x, int y, int width, int height) {
        super(app, x, y, width, height);

        this.focusOnClick = false;
        setStyle("background", background);
    }

    @Override
    public void renderContent(VRenderContext ctx) {
        var state = createStyleState();

        Vera.renderer.drawFill(ctx, 0, 0, width, height, getStyle("background", state));
    }
}
