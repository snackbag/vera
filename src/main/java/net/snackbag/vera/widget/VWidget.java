package net.snackbag.vera.widget;

import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VeraApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class VWidget {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double rotation;

    protected VeraApp app;
    private boolean hovered = false;
    private final HashMap<String, List<Runnable>> eventExecutors;

    public VWidget(int x, int y, int width, int height, VeraApp app) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.app = app;
        this.rotation = 0;
        this.eventExecutors = new HashMap<>();
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

    public void update() {}

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public void onHover(Runnable runnable) {
        registerEventExecutor("hover", runnable);
    }

    public void registerEventExecutor(String event, Runnable runnable) {
        if (!eventExecutors.containsKey(event)) eventExecutors.put(event, new ArrayList<>());
        eventExecutors.get(event).add(runnable);
    }

    public void fireEvent(String event) {
        if (!eventExecutors.containsKey(event)) return;
        eventExecutors.get(event).parallelStream().forEach((Runnable::run));
    }
}
