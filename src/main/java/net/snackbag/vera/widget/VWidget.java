package net.snackbag.vera.widget;

import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VEvent;
import net.snackbag.vera.event.VMouseDragEvent;
import net.snackbag.vera.event.VMouseMoveEvent;
import net.snackbag.vera.event.VMouseScrollEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public abstract class VWidget<T extends VWidget<T>> {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double rotation;

    protected VeraApp app;
    protected @Nullable VCursorShape hoverCursor = null;
    protected @Nullable VCursorShape cursorBeforeHover = null;
    protected boolean focusOnClick = true;
    private boolean hovered = false;
    private boolean visible = true;

    private boolean leftClickDown = false;
    private boolean middleClickDown = false;
    private boolean rightClickDown = false;
    private int leftDragPreviousX = -1;
    private int leftDragPreviousY = -1;
    private int middleDragPreviousX = -1;
    private int middleDragPreviousY = -1;
    private int rightDragPreviousX = -1;
    private int rightDragPreviousY = -1;

    private final HashMap<String, List<VEvent>> eventExecutors;
    private final List<Supplier<Boolean>> visibilityConditions;

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
        registerEventExecutor("mouse-drag-left", args -> runnable.run((int) args[0], (int) args[1], (int) args[2], (int) args[3]));
    }

    public void onMouseDragRight(VMouseDragEvent runnable) {
        registerEventExecutor("mouse-drag-right", args -> runnable.run((int) args[0], (int) args[1], (int) args[2], (int) args[3]));
    }

    public void onMouseDragMiddle(VMouseDragEvent runnable) {
        registerEventExecutor("mouse-drag-middle", args -> runnable.run((int) args[0], (int) args[1], (int) args[2], (int) args[3]));
    }

    public void onFocusStateChange(Runnable runnable) {
        registerEventExecutor("focus-state-change", runnable);
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

    public void handleBuiltinEvent(String event, Object... args) {
        switch (event) {
            case "left-click" -> {
                if (shouldFocusOnClick()) {
                    setFocused(true);
                }
                leftClickDown = true;
            }

            case "right-click" -> rightClickDown = true;
            case "middle-click" -> middleClickDown = true;

            case "left-click-release" -> clearLeftClickDown();
            case "right-click-release" -> clearRightClickDown();
            case "middle-click-release" -> clearMiddleClickDown();

            case "mouse-move" -> {
                if (leftClickDown) {
                    int newX = (int) args[0];
                    int newY = (int) args[1];
                    if (leftDragPreviousX != -1 || leftDragPreviousY != -1) fireEvent("mouse-drag-left", leftDragPreviousX, leftDragPreviousY, newX, newY);

                    leftDragPreviousX = newX;
                    leftDragPreviousY = newY;
                } else if (rightClickDown) {
                    int newX = (int) args[0];
                    int newY = (int) args[1];

                    if (rightDragPreviousX != -1 || rightDragPreviousY != -1) fireEvent("mouse-drag-right", rightDragPreviousX, rightDragPreviousY, newX, newY);

                    rightDragPreviousX = newX;
                    rightDragPreviousY = newY;
                } else if (middleClickDown) {
                    int newX = (int) args[0];
                    int newY = (int) args[1];

                    if (middleDragPreviousX != -1 || middleDragPreviousY != -1) fireEvent("mouse-drag-middle", middleDragPreviousX, middleDragPreviousY, newX, newY);

                    middleDragPreviousX = newX;
                    middleDragPreviousY = newY;
                }
            }

            case "hover" -> {
                if (hoverCursor == null) break;

                cursorBeforeHover = app.getCursorShape();
                app.setCursorShape(hoverCursor);
            }

            case "hover-leave" -> {
                clearLeftClickDown();
                clearRightClickDown();
                clearMiddleClickDown();

                if (cursorBeforeHover == null) break;

                app.setCursorShape(cursorBeforeHover);
            }
        }
    }

    private void clearLeftClickDown() {
        leftClickDown = false;
        leftDragPreviousX = -1;
        leftDragPreviousY = -1;
    }

    private void clearRightClickDown() {
        rightClickDown = false;
        rightDragPreviousX = -1;
        rightDragPreviousY = -1;
    }

    private void clearMiddleClickDown() {
        middleClickDown = false;
        middleDragPreviousX = -1;
        middleDragPreviousY = -1;
    }

    public @Nullable VCursorShape getHoverCursor() {
        return hoverCursor;
    }

    public void setHoverCursor(@Nullable VCursorShape hoverCursor) {
        this.hoverCursor = hoverCursor;
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
