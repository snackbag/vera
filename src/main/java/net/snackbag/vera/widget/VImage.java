package net.snackbag.vera.widget;

import net.snackbag.vera.VeraRenderer;
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
        VeraRenderer renderer = getRenderer();
        VeraApp app = getApp();

        renderer.drawImage(app, x + app.getX(), y + app.getY(), width, height, rotation, path);
    }
}
