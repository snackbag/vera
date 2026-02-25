package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.core.VRenderContext;

public class VRect extends VWidget<VRect> {
    public VRect(VColor color, VeraApp app) {
        this(color, 0, 0, 20, 20, app);
    }

    public VRect(VColor color, int x, int y, VeraApp app) {
        this(color, x, y, 20, 20, app);
    }

    public VRect(VColor color, int x, int y, int width, int height, VeraApp app) {
        super(x, y, width, height, app);

        this.focusOnClick = false;
        setStyle("background-color", color);
    }

    @Override
    public void render(VRenderContext ctx) {
        VStyleState state = createStyleState();

        Vera.renderer.drawRect(ctx, 0, 0, width, height, getStyle("background-color", state));
    }
}
