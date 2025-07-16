package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleState;

public class VRect extends VWidget<VRect> {
    public VRect(VColor color, VeraApp app) {
        super(0, 0, 20, 20, app);

        this.focusOnClick = false;
    }

    @Override
    public void render() {
        StyleState state = createStyleState();

        Vera.renderer.drawRect(app, getX(), getY(), width, height, rotation, getStyle("color", state));
    }
}
