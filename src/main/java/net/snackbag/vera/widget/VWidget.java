package net.snackbag.vera.widget;

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
    private boolean visible = true;
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


    public int getRealX() {
        return getX();
    }

    public int getRealY() {
        return getY();
    }

    public int getWidth() {
        return width;
    }

    public int getFullWidth() {
        return getWidth();
    }

    public int getHeight() {
        return height;
    }

    public int getFullHeight() {
        return getHeight();
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

    public void update() {}

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        // If changed
        if (this.hovered != hovered) {
            if (hovered) fireEvent("hover");
            else fireEvent("hover-leave");
        }

        this.hovered = hovered;
    }

    public void onHover(Runnable runnable) {
        registerEventExecutor("hover", runnable);
    }

    public void onHoverLeave(Runnable runnable) {
        registerEventExecutor("hover-leave", runnable);
    }

    public void onLeftClick(Runnable runnable) {
        registerEventExecutor("left-click", runnable);
    }

    public void onRightClick(Runnable runnable) {
        registerEventExecutor("right-click", runnable);
    }

    public void onMiddleClick(Runnable runnable) {
        registerEventExecutor("middle-click", runnable);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void registerEventExecutor(String event, Runnable runnable) {
        if (!eventExecutors.containsKey(event)) eventExecutors.put(event, new ArrayList<>());
        eventExecutors.get(event).add(runnable);
    }

    public void fireEvent(String event) {
        if (!eventExecutors.containsKey(event)) return;
        eventExecutors.get(event).parallelStream().forEach((Runnable::run));
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public void charTyped(char chr, int modifiers) {}
}
