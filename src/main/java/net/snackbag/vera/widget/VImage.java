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

        renderer.drawImage(app, x, y, width, height, rotation, path);
    }
}
