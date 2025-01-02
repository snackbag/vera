package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

public class VImage extends VWidget<VImage> {
    private Identifier path;

    public VImage(Identifier path, int width, int height, VeraApp app) {
        super(0, 0, width, height, app);

        this.path = path;
        this.focusOnClick = false;
    }

    public Identifier getPath() {
        return path;
    }

    public void setPath(Identifier path) {
        this.path = path;
    }

    public void setPath(String path) {
        setPath(new Identifier(path));
    }

    @Override
    public void render() {
        VeraApp app = getApp();
        Vera.renderer.drawImage(app, x, y, width, height, rotation, path);
    }
}
