package net.snackbag.vera.widget;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VEvent;
import net.snackbag.vera.event.VScrollEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class VWidget<T extends VWidget<T>> {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double rotation;

    protected VeraApp app;
    protected boolean focusOnClick = true;
    private boolean hovered = false;
    private boolean visible = true;

    private final HashMap<String, List<VEvent>> eventExecutors;

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

    public int getHitboxX() {
        return getX();
    }

    public int getHitboxY() {
        return getY();
    }

    public int getWidth() {
        return width;
    }

    public int getHitboxWidth() {
        return getWidth();
    }

    public int getHeight() {
        return height;
    }

    public int getHitboxHeight() {
        return getHeight();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
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

    public void onLeftClickRelease(Runnable runnable) {
        registerEventExecutor("left-click-release", runnable);
    }

    public void onRightClick(Runnable runnable) {
        registerEventExecutor("right-click", runnable);
    }

    public void onRightClickRelease(Runnable runnable) {
        registerEventExecutor("right-click-release", runnable);
    }

    public void onMiddleClick(Runnable runnable) {
        registerEventExecutor("middle-click", runnable);
    }

    public void onMiddleClickRelease(Runnable runnable) {
        registerEventExecutor("right-click-release", runnable);
    }

    public void onMouseScroll(Consumer<Integer> runnable) {
        registerEventExecutor("mouse-scroll", args -> runnable.accept((int) args[0]));
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

    public void registerEventExecutor(String event, VEvent executor) {
        eventExecutors.computeIfAbsent(event, k -> new ArrayList<>()).add(executor);
    }

    public void registerEventExecutor(String event, Runnable runnable) {
        registerEventExecutor(event, args -> runnable.run());
    }

    public void fireEvent(String event, Object... args) {
        handleBuiltinEvent(event);

        if (!eventExecutors.containsKey(event)) return;
        eventExecutors.get(event).parallelStream().forEach(e -> e.run(args));
    }

    public void handleBuiltinEvent(String event) {
        if (event.equals("left-click")) {
            if (shouldFocusOnClick()) {
                setFocused(true);
            }
        }
    }

    public boolean isFocused() {
        return app.isFocusedWidget(this);
    }

    public void setFocused(boolean focused) {
        if (focused) app.setFocusedWidget(this);
        else app.setFocusedWidget(null);
    }

    public boolean shouldFocusOnClick() {
        return focusOnClick;
    }

    public void setFocusOnClick(boolean focus) {
        focusOnClick = focus;
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public void charTyped(char chr, int modifiers) {}

    public T alsoAdd() {
        app.addWidget(this);
        return (T) this;
    }
}
