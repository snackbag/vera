package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleState;

public class VImage extends VWidget<VImage> {
    public VImage(Identifier src, int width, int height, VeraApp app) {
        super(0, 0, width, height, app);

        setStyle("src", src);
        this.focusOnClick = false;
    }

    public VImage(String src, int width, int height, VeraApp app) {
        this(new Identifier(src), width, height, app);
    }

    @Override
    public void render() {
        StyleState state = createStyleState();
        Identifier src = getStyle("src", state);

        Vera.renderer.drawImage(app, getX(), getY(), width, height, rotation, src);
    }
}
