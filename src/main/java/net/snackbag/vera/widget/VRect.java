package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VFill;
import net.snackbag.vera.core.VImage;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.core.VRenderContext;

public class VRect extends VWidget<VRect> {
    public VRect(VFill background, VeraApp app) {
        this(background, 0, 0, 20, 20, app);
    }

    public VRect(VFill background, int x, int y, VeraApp app) {
        this(background, x, y, 20, 20, app);
    }

    public VRect(VFill background, int x, int y, int width, int height, VeraApp app) {
        super(x, y, width, height, app);

        this.focusOnClick = false;
        setStyle("background", background);
    }

    @Override
    public void render(VRenderContext ctx) {
        VStyleState state = createStyleState();

        Vera.renderer.drawFill(ctx, 0, 0, width, height, getStyle("background", state));
    }
}
