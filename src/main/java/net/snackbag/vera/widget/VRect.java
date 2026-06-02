package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.style.VInteractionState;

public class VRect extends VWidget<VRect> {
    public VRect(VFill background, VAppAccess app) {
        this(background, 0, 0, 20, 20, app);
    }

    public VRect(VFill background, int x, int y, VAppAccess app) {
        this(background, x, y, 20, 20, app);
    }

    public VRect(VFill background, int x, int y, int width, int height, VAppAccess app) {
        super(x, y, width, height, app);

        this.focusOnClick = false;
        setStyle("background", background);
    }

    @Override
    public void renderContent(VRenderContext ctx) {
        VInteractionState state = createStyleState();

        Vera.renderer.drawFill(ctx, 0, 0, width, height, getStyle("background", state));
    }
}
