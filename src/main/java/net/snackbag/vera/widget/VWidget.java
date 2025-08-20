package net.snackbag.vera.widget;

import net.snackbag.vera.VElement;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.core.v4.V4Color;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.event.*;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;
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

    public final AnimationEngine animations = new AnimationEngine(this);
    public final LinkedHashSet<String> classes = new LinkedHashSet<>();

    public VWidget(int x, int y, int width, int height, VeraApp app) {
        super(app, x, y, width, height);

        this.rotation = 0;
    }

    public abstract void render();

    public int getHitboxX() {
        return getEffectiveX();
    }

    public int getHitboxY() {
        return getEffectiveY();
    }

    public int getHitboxWidth() {
        return getEffectiveWidth();
    }

    public int getHitboxHeight() {
        return getEffectiveHeight();
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
        return animations.animateStyle(key, app.styleSheet.getKey(this, key));
    }

    public <V> V getStyle(String key, StyleState state) {
        return animations.animateStyle(key, app.styleSheet.getKey(this, key, state));
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
        Vera.renderer.drawRect(app, getEffectiveX(), getEffectiveY() - borderSize.get1(), getEffectiveWidth(), borderSize.get1(), 0, borderColor.get1());
        if (borderSize.get3() > 0) {
            Vera.renderer.drawRect(app, getEffectiveX() - borderSize.get3(), getEffectiveY() - borderSize.get1(), borderSize.get3(), borderSize.get1(), 0, borderColor.get1());
        }

        // Bottom
        Vera.renderer.drawRect(app, getEffectiveX(), getEffectiveY() + getEffectiveHeight(), getEffectiveWidth(), borderSize.get2(), 0, borderColor.get2());
        if (borderSize.get4() > 0) {
            Vera.renderer.drawRect(app, getEffectiveX() + getEffectiveWidth(), getEffectiveY() + getEffectiveHeight(), borderSize.get4(), borderSize.get2(), 0, borderColor.get2());
        }

        // Left
        Vera.renderer.drawRect(app, getEffectiveX() - borderSize.get3(), getEffectiveY(), borderSize.get3(), getEffectiveHeight(), 0, borderColor.get3());
        if (borderSize.get2() > 0) {
            Vera.renderer.drawRect(app, getEffectiveX() - borderSize.get3(), getEffectiveY() + getEffectiveHeight(), borderSize.get3(), borderSize.get2(), 0, borderColor.get3());
        }

        // Right
        Vera.renderer.drawRect(app, getEffectiveX() + getEffectiveWidth(), getEffectiveY(), borderSize.get4(), getEffectiveHeight(), 0, borderColor.get4());
        if (borderSize.get1() > 0) {
            Vera.renderer.drawRect(app, getEffectiveX() + getEffectiveWidth(), getEffectiveY() - borderSize.get1(), borderSize.get4(), borderSize.get1(), 0, borderColor.get4());
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
            if (hovered) events.fire(Events.Widget.HOVER);
            else events.fire(Events.Widget.HOVER_LEAVE);
        }

        this.hovered = hovered;
    }

    public void onHover(Runnable runnable) {
        events.register(Events.Widget.HOVER, runnable);
    }

    public void onHoverLeave(Runnable runnable) {
        events.register(Events.Widget.HOVER_LEAVE, runnable);
    }

    public void onLeftClick(Runnable runnable) {
        events.register(Events.Widget.LEFT_CLICK, runnable);
    }

    public void onLeftClickRelease(Runnable runnable) {
        events.register(Events.Widget.LEFT_CLICK_RELEASE, runnable);
    }

    public void onRightClick(Runnable runnable) {
        events.register(Events.Widget.RIGHT_CLICK, runnable);
    }

    public void onRightClickRelease(Runnable runnable) {
        events.register(Events.Widget.RIGHT_CLICK_RELEASE, runnable);
    }

    public void onMiddleClick(Runnable runnable) {
        events.register(Events.Widget.MIDDLE_CLICK, runnable);
    }

    public void onMiddleClickRelease(Runnable runnable) {
        events.register(Events.Widget.MIDDLE_CLICK_RELEASE, runnable);
    }

    public void onMouseScroll(VMouseScrollEvent runnable) {
        events.register(Events.Widget.SCROLL, args -> runnable.run(
                (int) args[0], (int) args[1], (double) args[2])
        );
    }

    public void onMouseMove(VMouseMoveEvent runnable) {
        events.register(Events.Widget.MOUSE_MOVE, args -> runnable.run((int) args[0], (int) args[1]));
    }

    public void onMouseDragLeft(VMouseDragEvent runnable) {
        events.register(Events.Widget.DRAG_LEFT_CLICK, args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragRight(VMouseDragEvent runnable) {
        events.register(Events.Widget.DRAG_RIGHT_CLICK, args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragMiddle(VMouseDragEvent runnable) {
        events.register(Events.Widget.DRAG_MIDDLE_CLICK, args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onFocusStateChange(Runnable runnable) {
        events.register(Events.Widget.FOCUS_STATE_CHANGE, runnable);
    }

    public void onFilesDropped(VFilesDroppedEvent runnable) {
        events.register(Events.Widget.FILES_DROPPED, args -> runnable.run((List<Path>) args[0]));
    }

    public void onAnimationBegin(VAnimationBeginEvent runnable) {
        events.register(Events.Animation.BEGIN, args -> runnable.run((VAnimation) args[0]));
    }

    public void onAnimationUnwindBegin(VAnimationUnwindEvent runnable) {
        events.register(Events.Animation.UNWIND_BEGIN, args -> runnable.run((VAnimation) args[0]));
    }

    public void onAnimationRewindBegin(VAnimationRewindEvent runnable) {
        events.register(Events.Animation.REWIND_BEGIN, args -> runnable.run((VAnimation) args[0]));
    }

    public void onAnimationFinish(VAnimationFinishEvent runnable) {
        events.register(Events.Animation.FINISH, args -> runnable.run((VAnimation) args[0], (long) args[1]));
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        switch (event) {
            case Events.Widget.LEFT_CLICK -> {
                if (focusOnClick) {
                    setFocused(true);
                }
                leftClickDown = true;
            }

            case Events.Widget.RIGHT_CLICK -> rightClickDown = true;
            case Events.Widget.MIDDLE_CLICK -> middleClickDown = true;

            case Events.Widget.LEFT_CLICK_RELEASE -> clearLeftClickDown();
            case Events.Widget.RIGHT_CLICK_RELEASE -> clearRightClickDown();
            case Events.Widget.MIDDLE_CLICK_RELEASE -> clearMiddleClickDown();

            case Events.Widget.HOVER_LEAVE -> {
                clearLeftClickDown();
                clearRightClickDown();
                clearMiddleClickDown();
            }
        }
    }

    @Override
    public void afterBuiltinEvent(String name, Object... args) {
        updateIfNeeded();
    }

    private void updateIfNeeded() {
        StyleState state = createStyleState();
        if (state != prevStyleState) {
            update();
            prevStyleState = state;
        }
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
