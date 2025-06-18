package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleState;

public class VImage extends VWidget<VImage> {
    public VImage(Identifier path, int width, int height, VeraApp app) {
        super(0, 0, width, height, app);

        setStyle("src", path);
        this.focusOnClick = false;
    }

    public VImage(String path, int width, int height, VeraApp app) {
        this(new Identifier(path), width, height, app);
    }

    @Override
    public void render() {
        VeraApp app = getApp();

        StyleState state = createStyleState();
        Identifier src = getStyle("src", state);

        Vera.renderer.drawImage(app, x, y, width, height, rotation, src);
    }
}
