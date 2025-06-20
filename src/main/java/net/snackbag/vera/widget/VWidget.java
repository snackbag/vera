package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.*;
import net.snackbag.vera.style.StyleState;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public abstract class VWidget<T extends VWidget<T>> {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double rotation;

    protected VeraApp app;
    public boolean focusOnClick = true;
    private boolean hovered = false;
    private boolean visible = true;

    private boolean leftClickDown = false;
    private boolean middleClickDown = false;
    private boolean rightClickDown = false;
    private StyleState prevStyleState = StyleState.DEFAULT;

    private final HashMap<String, List<VEvent>> eventExecutors;
    private final List<Supplier<Boolean>> visibilityConditions;
    public final LinkedHashSet<String> classes = new LinkedHashSet<>();

    public VWidget(int x, int y, int width, int height, VeraApp app) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.app = app;
        this.rotation = 0;
        this.eventExecutors = new HashMap<>();
        this.visibilityConditions = new ArrayList<>();

        addVisibilityCondition(this::isVisible);

        setStyle("overlay", VColor.transparent());
        setStyle("cursor", VCursorShape.DEFAULT);

        // Border
        setStyle("border-color", new V4Color(VColor.black()));
        setStyle("border-size", new V4Int(0));
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

    public void setStyle(String key, Object value) {
        app.styleSheet.setKey(this, key, value);
    }

    public void setStyle(String key, Object value, StyleState state) {
        app.styleSheet.setKey(this, key, value, state);
    }

    public <V> V getStyle(String key) {
        return app.styleSheet.getKey(this, key);
    }

    public <V> V getStyle(String key, StyleState state) {
        return app.styleSheet.getKey(this, key, state);
    }

    public <V> V getStyleOrDefault(String key, V dflt) {
        V style = getStyle(key);
        return style != null ? style : dflt;
    }

    public <V> V getStyleOrDefault(String key, V dflt, StyleState state) {
        V style = getStyle(key, state);
        return style != null ? style : dflt;
    }

    public StyleState createStyleState() {
        if (isHovered()) return StyleState.HOVERED;
        else return StyleState.DEFAULT;
    }

    public void renderBorder() {
        // TODO: [Render Rework] Better border rendering

        StyleState state = createStyleState();

        V4Color borderColor = getStyle("border-color", state);
        V4Int borderSize = getStyle("border-size", state);

        // Top
        Vera.renderer.drawRect(app, getHitboxX(), getHitboxY() - borderSize.get1(), getHitboxWidth(), borderSize.get1(), 0, borderColor.get1());
        if (borderSize.get3() > 0) {
            Vera.renderer.drawRect(app, getHitboxX() - borderSize.get3(), getHitboxY() - borderSize.get1(), borderSize.get3(), borderSize.get1(), 0, borderColor.get1());
        }

        // Bottom
        Vera.renderer.drawRect(app, getHitboxX(), getHitboxY() + getHitboxHeight(), getHitboxWidth(), borderSize.get2(), 0, borderColor.get2());
        if (borderSize.get4() > 0) {
            Vera.renderer.drawRect(app, getHitboxX() + getHitboxWidth(), getHitboxY() + getHitboxHeight(), borderSize.get4(), borderSize.get2(), 0, borderColor.get2());
        }

        // Left
        Vera.renderer.drawRect(app, getHitboxX() - borderSize.get3(), getHitboxY(), borderSize.get3(), getHitboxHeight(), 0, borderColor.get3());
        if (borderSize.get2() > 0) {
            Vera.renderer.drawRect(app, getHitboxX() - borderSize.get3(), getHitboxY() + getHitboxHeight(), borderSize.get3(), borderSize.get2(), 0, borderColor.get3());
        }

        // Right
        Vera.renderer.drawRect(app, getHitboxX() + getHitboxWidth(), getHitboxY(), borderSize.get4(), getHitboxHeight(), 0, borderColor.get4());
        if (borderSize.get1() > 0) {
            Vera.renderer.drawRect(app, getHitboxX() + getHitboxWidth(), getHitboxY() - borderSize.get1(), borderSize.get4(), borderSize.get1(), 0, borderColor.get4());
        }
    }

    public void renderOverlay() {
        StyleState state = createStyleState();

        Vera.renderer.drawRect(app, x, y, width, height, 0, getStyle("overlay", state));
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setSize(int all) {
        setSize(all, all);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int both) {
        move(both, both);
    }

    public boolean isLeftClickDown() {
        return leftClickDown;
    }

    public boolean isMiddleClickDown() {
        return middleClickDown;
    }

    public boolean isRightClickDown() {
        return rightClickDown;
    }

    public boolean isAnyMouseButtonDown() {
        return leftClickDown || middleClickDown || rightClickDown;
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

    public void update() {
        StyleState state = createStyleState();

        app.setCursorShape(getStyle("cursor", state));
    }

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
        registerEventExecutor("middle-click-release", runnable);
    }

    public void onMouseScroll(VMouseScrollEvent runnable) {
        registerEventExecutor("mouse-scroll", args -> runnable.run(
                (int) args[0], (int) args[1], (double) args[2])
        );
    }

    public void onMouseMove(VMouseMoveEvent runnable) {
        registerEventExecutor("mouse-move", args -> runnable.run((int) args[0], (int) args[1]));
    }

    public void onMouseDragLeft(VMouseDragEvent runnable) {
        registerEventExecutor("mouse-drag-left", args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragRight(VMouseDragEvent runnable) {
        registerEventExecutor("mouse-drag-right", args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragMiddle(VMouseDragEvent runnable) {
        registerEventExecutor("mouse-drag-middle", args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onFocusStateChange(Runnable runnable) {
        registerEventExecutor("focus-state-change", runnable);
    }

    public void onFilesDropped(VFilesDroppedEvent runnable) {
        registerEventExecutor("files-dropped", args -> runnable.run((List<Path>) args[0]));
    }

    public void onMessage(VWidgetMessageEvent runnable) {
        registerEventExecutor("widget-message", args -> runnable.run((VWidgetMessageEvent.Context) args[0]));
    }

    public void sendMessage(VWidget<?> widget, String type) {
        sendMessage(widget, type, null);
    }

    public void sendMessage(VWidget<?> widget, String type, @Nullable Object content) {
        widget.fireEvent("widget-message", new VWidgetMessageEvent.Context(this, type, content));
    }

    public void sendMessageAll(String type) {
        sendMessageAll(type, null);
    }

    public void sendMessageAll(String type, @Nullable Object content) {
        VWidgetMessageEvent.Context ctx = new VWidgetMessageEvent.Context(this, type, content);
        for (VWidget<?> widget : app.getWidgets()) widget.fireEvent("widget-message", ctx);
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
        handleBuiltinEvent(event, args);

        if (!eventExecutors.containsKey(event)) return;
        eventExecutors.get(event).parallelStream().forEach(e -> e.run(args));
    }

    public void clearEvents() {
        eventExecutors.clear();
    }

    public void clearEventsFor(String event) {
        // IDE said I don't need a containsKey check
        eventExecutors.remove(event);
    }

    public void handleBuiltinEvent(String event, Object... args) {
        StyleState state = createStyleState();
        if (state != prevStyleState) update();

        switch (event) {
            case "left-click" -> {
                if (focusOnClick) {
                    setFocused(true);
                }
                leftClickDown = true;
            }

            case "right-click" -> rightClickDown = true;
            case "middle-click" -> middleClickDown = true;

            case "left-click-release" -> clearLeftClickDown();
            case "right-click-release" -> clearRightClickDown();
            case "middle-click-release" -> clearMiddleClickDown();

            case "hover" -> app.setCursorShape(getStyle("cursor", state));
            case "hover-leave" -> {
                clearLeftClickDown();
                clearRightClickDown();
                clearMiddleClickDown();
            }
        }

        prevStyleState = state;
    }

    private void clearLeftClickDown() {
        leftClickDown = false;
    }

    private void clearRightClickDown() {
        rightClickDown = false;
    }

    private void clearMiddleClickDown() {
        middleClickDown = false;
    }

    public boolean isFocused() {
        return app.isFocusedWidget(this);
    }

    public void setFocused(boolean focused) {
        if (focused) app.setFocusedWidget(this);
        else app.setFocusedWidget(null);
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public void charTyped(char chr, int modifiers) {}

    public void remove() {
        app.removeWidget(this);
    }

    public T alsoAdd() {
        app.addWidget(this);
        return (T) this;
    }

    public void addVisibilityCondition(Supplier<Boolean> condition) {
        visibilityConditions.add(condition);
    }

    public boolean visibilityConditionsPassed() {
        return visibilityConditions.parallelStream().allMatch(Supplier::get);
    }
}
