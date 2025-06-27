package net.snackbag.vera.widget;

import net.snackbag.vera.VElement;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.*;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.util.DragHandler;

import java.nio.file.Path;
import java.util.*;

public abstract class VWidget<T extends VWidget<T>> extends VElement {
    protected double rotation;

    public boolean focusOnClick = true;
    private boolean hovered = false;

    private boolean leftClickDown = false;
    private boolean middleClickDown = false;
    private boolean rightClickDown = false;
    private StyleState prevStyleState = StyleState.DEFAULT;

    public final LinkedHashSet<String> classes = new LinkedHashSet<>();

    public VWidget(int x, int y, int width, int height, VeraApp app) {
        super(app, x, y, width, height);

        this.rotation = 0;

        setStyle("overlay", VColor.transparent());
        setStyle("cursor", VCursorShape.DEFAULT);

        // Border
        setStyle("border-color", new V4Color(VColor.black()));
        setStyle("border-size", new V4Int(0));
    }

    public abstract void render();

    public int getHitboxX() {
        return getX();
    }

    public int getHitboxY() {
        return getY();
    }

    public int getHitboxWidth() {
        return getWidth();
    }

    public int getHitboxHeight() {
        return getHeight();
    }

    @SuppressWarnings("unchecked")
    public <V> void setStyle(String key, V... value) {
        app.styleSheet.setKey(this, key, value);
    }

    @SuppressWarnings("unchecked")
    public <V> void setStyle(String key, StyleState state, V... value) {
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
        // Clicks first
        if (leftClickDown) return StyleState.LEFT_CLICKED;
        else if (middleClickDown) return StyleState.MIDDLE_CLICKED;
        else if (rightClickDown) return StyleState.RIGHT_CLICKED;

        else if (DragHandler.isDragging() && DragHandler.target == this) {
            return switch (DragHandler.button) {
                case LEFT -> StyleState.LC_DRAGGING;
                case MIDDLE -> StyleState.MC_DRAGGING;
                case RIGHT -> StyleState.RC_DRAGGING;
            };
        }

        // Hover as last, since everything else is hover too
        else if (isHovered()) return StyleState.HOVERED;
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

        Vera.renderer.drawRect(app, getX(), getY(), width, height, 0, getStyle("overlay", state));
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
            if (hovered) events.fire("hover");
            else events.fire("hover-leave");
        }

        this.hovered = hovered;
    }

    public void onHover(Runnable runnable) {
        events.register("hover", runnable);
    }

    public void onHoverLeave(Runnable runnable) {
        events.register("hover-leave", runnable);
    }

    public void onLeftClick(Runnable runnable) {
        events.register("left-click", runnable);
    }

    public void onLeftClickRelease(Runnable runnable) {
        events.register("left-click-release", runnable);
    }

    public void onRightClick(Runnable runnable) {
        events.register("right-click", runnable);
    }

    public void onRightClickRelease(Runnable runnable) {
        events.register("right-click-release", runnable);
    }

    public void onMiddleClick(Runnable runnable) {
        events.register("middle-click", runnable);
    }

    public void onMiddleClickRelease(Runnable runnable) {
        events.register("middle-click-release", runnable);
    }

    public void onMouseScroll(VMouseScrollEvent runnable) {
        events.register("mouse-scroll", args -> runnable.run(
                (int) args[0], (int) args[1], (double) args[2])
        );
    }

    public void onMouseMove(VMouseMoveEvent runnable) {
        events.register("mouse-move", args -> runnable.run((int) args[0], (int) args[1]));
    }

    public void onMouseDragLeft(VMouseDragEvent runnable) {
        events.register("mouse-drag-left", args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragRight(VMouseDragEvent runnable) {
        events.register("mouse-drag-right", args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragMiddle(VMouseDragEvent runnable) {
        events.register("mouse-drag-middle", args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onFocusStateChange(Runnable runnable) {
        events.register("focus-state-change", runnable);
    }

    public void onFilesDropped(VFilesDroppedEvent runnable) {
        events.register("files-dropped", args -> runnable.run((List<Path>) args[0]));
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        StyleState state = createStyleState();
        if (state != prevStyleState) update();
        prevStyleState = state;

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

        state = createStyleState();
        if (state != prevStyleState) update();
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

    public T alsoAddClass(String clazz) {
        classes.add(clazz);
        return (T) this;
    }

    public T alsoAdd() {
        app.addWidget(this);
        return (T) this;
    }

    @Override
    public T alsoAddTo(VLayout layout) {
        super.alsoAddTo(layout);
        alsoAdd();

        return (T) this;
    }
}
