package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

public class VImage extends VWidget<VImage> {
    public VImage(Identifier path, int width, int height, VeraApp app) {
        super(0, 0, width, height, app);

        setStyle("src", path);
        this.focusOnClick = false;
    }

    @Override
    public void render() {
        VeraApp app = getApp();
        Vera.renderer.drawImage(app, x, y, width, height, rotation, getStyle("src"));
    }
}
