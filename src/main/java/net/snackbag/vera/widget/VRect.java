package net.snackbag.vera.widget;

import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;

public class VRect extends VWidget {
    protected VColor color;

    public VRect(VColor color, VeraApp app) {
        super(0, 0, 20, 20, app);

        this.color = color;
    }

    public VColor getColor() {
        return color;
    }

    public void setColor(VColor color) {
        this.color = color;
    }

    @Override
    public void render() {
        VeraRenderer renderer = getRenderer();

        renderer.drawRect(getApp(), x + app.getX(), y + app.getY(), width, height, rotation, color);
    }
}
