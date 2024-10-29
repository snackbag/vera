package net.snackbag.vera.widget;

import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VeraApp;

public abstract class VWidget {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double rotation;

    protected VeraApp app;

    public VWidget(int x, int y, int width, int height, VeraApp app) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.app = app;
        this.rotation = 0;
    }

    public abstract void render();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int both) {
        move(both, both);
    }

    public VeraApp getApp() {
        return app;
    }

    public double getRotation() {
        return rotation;
    }

    public void rotate(double rotation) {
        this.rotation = rotation;
    }

    protected VeraProvider getProvider() {
        return app.getProvider();
    }

    protected VeraRenderer getRenderer() {
        return app.getProvider().getRenderer();
    }
}
