package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

public class VImage extends VWidget {
    private String path;

    public VImage(String path, int width, int height, VeraApp app) {
        super(0, 0, width, height, app);

        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void render() {
        VeraApp app = getApp();
        Vera.renderer.drawImage(app, x + app.getX(), y + app.getY(), width, height, rotation, path);
    }
}
