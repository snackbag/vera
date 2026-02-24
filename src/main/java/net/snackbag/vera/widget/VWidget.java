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
import net.snackbag.vera.style.animation.CompiledAnimation;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.util.DragHandler;

import java.nio.file.Path;
import java.util.*;

public abstract class VWidget<T extends VWidget<T>> extends VElement {
    public AnimationEngine animations = new AnimationEngine(this);
    protected double rotation;
    protected boolean hasTransparency;

    public boolean focusOnClick = true;
    private boolean hovered = false;

    private boolean leftClickDown = false;
    private boolean middleClickDown = false;
    private boolean rightClickDown = false;
    private StyleState handledPrevStyleState = StyleState.DEFAULT; // constantly updates
    private StyleState prevStyleState = StyleState.DEFAULT; // updates max once per frame, can be seen as the definite result

    private StyleState transitionOrigin = null;
    private boolean isTransitionUnwinding = false;

    public final LinkedHashSet<String> classes = new LinkedHashSet<>();

    public VWidget(int x, int y, int width, int height, VeraApp app) {
        super(app, x, y, width, height);

        this.rotation = 0;
        this.hasTransparency = false;
    }

    public abstract void render(RenderContext ctx);

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

    public void animate(VAnimation animation) {
        animate(animation, false);
    }

    public void animate(VAnimation animation, boolean override) {
        if (override && isAnimationActive(animation.name)) stopAnimation(animation);
        animations.start(animation);
    }

    public void stopAnimation(VAnimation animation) {
        stopAnimation(animation.name);
    }

    public void stopAnimation(String animation) {
        animations.stop(animation);
    }

    public void stopAllAnimations() {
        for (String animation : animations.getActive()) {
            stopAnimation(animation);
        }
    }

    public void startOrRewindAnimation(VAnimation animation) {
        animations.startOrRewind(animation);
    }

    public void unwindAnimation(VAnimation animation) {
        unwindAnimation(animation.name);
    }

    public void unwindAnimation(String animation) {
        animations.unwind(animation);
    }

    public void rewindAnimation(VAnimation animation) {
        rewindAnimation(animation.name);
    }

    public void rewindAnimation(String animation) {
        animations.rewind(animation);
    }

    public boolean isAnimationActive(VAnimation animation) {
        return isAnimationActive(animation.name);
    }

    public boolean isAnimationActive(String animation) {
        return animations.isActive(animation);
    }

    public StyleState createStyleState() {
        return createStyleState(true);
    }

    public StyleState createStyleState(boolean respectAnimationLocks) {
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

    public RenderContext createRenderContext() {
        StyleState state = createStyleState();
        return new RenderContext(
                app.getX() + getEffectiveX(), app.getY() + getEffectiveY(),
                getEffectiveWidth(), getEffectiveHeight(),
                (float) rotation, getStyle("scale", state),
                hasTransparency
        );
    }

    public void renderBorder(RenderContext ctx) {
        // TODO: [Render Rework] Better border rendering

        StyleState state = createStyleState();

        V4Color borderColor = getStyle("border-color", state);
        V4Int borderSize = getStyle("border-size", state);

        // Top
        Vera.renderer.drawRect(ctx, 0, -borderSize.get1(), getEffectiveWidth(), borderSize.get1(), borderColor.get1());
        if (borderSize.get3() > 0) {
            Vera.renderer.drawRect(ctx, -borderSize.get3(), -borderSize.get1(), borderSize.get3(), borderSize.get1(), borderColor.get1());
        }

        // Bottom
        Vera.renderer.drawRect(ctx, 0, getEffectiveHeight(), getEffectiveWidth(), borderSize.get2(), borderColor.get2());
        if (borderSize.get4() > 0) {
            Vera.renderer.drawRect(ctx, getEffectiveWidth(), getEffectiveHeight(), borderSize.get4(), borderSize.get2(), borderColor.get2());
        }

        // Left
        Vera.renderer.drawRect(ctx, -borderSize.get3(), 0, borderSize.get3(), getEffectiveHeight(), borderColor.get3());
        if (borderSize.get2() > 0) {
            Vera.renderer.drawRect(ctx, -borderSize.get3(), getEffectiveHeight(), borderSize.get3(), borderSize.get2(), borderColor.get3());
        }

        // Right
        Vera.renderer.drawRect(ctx, getEffectiveWidth(), 0, borderSize.get4(), getEffectiveHeight(), borderColor.get4());
        if (borderSize.get1() > 0) {
            Vera.renderer.drawRect(ctx, getEffectiveWidth(), -borderSize.get1(), borderSize.get4(), borderSize.get1(), borderColor.get4());
        }
    }

    public void renderOverlay(RenderContext ctx) {
        StyleState state = createStyleState();

        Vera.renderer.drawRect(ctx, 0, 0, getEffectiveWidth(), getEffectiveHeight(), getStyle("overlay", state));
    }

    public void beforeRender() {
        StyleState state = createStyleState();

        if (state != prevStyleState) {
            Integer transitionTime = app.styleSheet.getKey(this, "transition", state);
            VEasing transitionEasing = app.styleSheet.getKey(this, "transition-easing", state);

            Transition: if (transitionTime > 0) {
                if (transitionOrigin == state) {
                    if (isTransitionUnwinding) rewindAnimation(VAnimation.INTERNAL_TRANSITION_NAME);
                    else unwindAnimation(VAnimation.INTERNAL_TRANSITION_NAME);
                    break Transition;
                }

                VAnimation.Builder builder = new VAnimation.Builder(VAnimation.INTERNAL_TRANSITION_NAME)
                        .relativeUnwindTime()
                        .unwindEasing(transitionEasing);

                builder.keyframe(0, 1, frame -> {
                    for (String key : app.styleSheet.getKeysStacked(this, prevStyleState)) {
                        frame.style(key, getStyle(key, prevStyleState));
                    }
                });

                builder.keyframe(transitionTime - 1, 0, frame -> {
                    for (String key : app.styleSheet.getKeysStacked(this, state)) {
                        frame.style(key, app.styleSheet.getKey(this, key, state));
                    }
                });

                transitionOrigin = prevStyleState;
                animate(builder.build(), true);
            }

            prevStyleState = state;
        }
    }

    public void afterRender() {
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

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
        events.fire(VEvents.Widget.TRANSPARENCY_STATE_CHANGED, hasTransparency);
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
            if (hovered) events.fire(VEvents.Widget.HOVER);
            else events.fire(VEvents.Widget.HOVER_LEAVE);
        }

        this.hovered = hovered;
    }

    public void onHover(Runnable runnable) {
        events.register(VEvents.Widget.HOVER, runnable);
    }

    public void onHoverLeave(Runnable runnable) {
        events.register(VEvents.Widget.HOVER_LEAVE, runnable);
    }

    public void onLeftClick(Runnable runnable) {
        events.register(VEvents.Widget.LEFT_CLICK, runnable);
    }

    public void onLeftClickRelease(Runnable runnable) {
        events.register(VEvents.Widget.LEFT_CLICK_RELEASE, runnable);
    }

    public void onRightClick(Runnable runnable) {
        events.register(VEvents.Widget.RIGHT_CLICK, runnable);
    }

    public void onRightClickRelease(Runnable runnable) {
        events.register(VEvents.Widget.RIGHT_CLICK_RELEASE, runnable);
    }

    public void onMiddleClick(Runnable runnable) {
        events.register(VEvents.Widget.MIDDLE_CLICK, runnable);
    }

    public void onMiddleClickRelease(Runnable runnable) {
        events.register(VEvents.Widget.MIDDLE_CLICK_RELEASE, runnable);
    }

    public void onMouseScroll(VMouseScrollEvent runnable) {
        events.register(VEvents.Widget.SCROLL, args -> runnable.run(
                (int) args[0], (int) args[1], (double) args[2])
        );
    }

    public void onMouseMove(VMouseMoveEvent runnable) {
        events.register(VEvents.Widget.MOUSE_MOVE, args -> runnable.run((int) args[0], (int) args[1]));
    }

    public void onMouseDragLeft(VMouseDragEvent runnable) {
        events.register(VEvents.Widget.DRAG_LEFT_CLICK, args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragRight(VMouseDragEvent runnable) {
        events.register(VEvents.Widget.DRAG_RIGHT_CLICK, args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onMouseDragMiddle(VMouseDragEvent runnable) {
        events.register(VEvents.Widget.DRAG_MIDDLE_CLICK, args -> runnable.run((VMouseDragEvent.Context) args[0]));
    }

    public void onFocusStateChange(Runnable runnable) {
        events.register(VEvents.Widget.FOCUS_STATE_CHANGE, runnable);
    }

    public void onFilesDropped(VFilesDroppedEvent runnable) {
        events.register(VEvents.Widget.FILES_DROPPED, args -> runnable.run((List<Path>) args[0]));
    }

    public void onAnimationBegin(VAnimationBeginEvent runnable) {
        events.register(VEvents.Animation.BEGIN, args -> runnable.run((VAnimation) args[0]));
    }

    public void onAnimationUnwindBegin(VAnimationUnwindEvent runnable) {
        events.register(VEvents.Animation.UNWIND_BEGIN, args -> runnable.run((VAnimation) args[0]));
    }

    public void onAnimationRewindBegin(VAnimationRewindEvent runnable) {
        events.register(VEvents.Animation.REWIND_BEGIN, args -> runnable.run((VAnimation) args[0]));
    }

    public void onAnimationFinish(VAnimationFinishEvent runnable) {
        events.register(VEvents.Animation.FINISH, args -> runnable.run((VAnimation) args[0], (long) args[1]));
    }

    public void onTransparencyStateChanged(VTransparencyStateChangedEvent runnable) {
        events.register(VEvents.Widget.TRANSPARENCY_STATE_CHANGED, args -> runnable.run((boolean) args[0]));
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        switch (event) {
            case VEvents.Widget.LEFT_CLICK -> {
                if (focusOnClick) {
                    setFocused(true);
                }
                leftClickDown = true;
            }

            case VEvents.Widget.RIGHT_CLICK -> rightClickDown = true;
            case VEvents.Widget.MIDDLE_CLICK -> middleClickDown = true;

            case VEvents.Widget.LEFT_CLICK_RELEASE -> clearLeftClickDown();
            case VEvents.Widget.RIGHT_CLICK_RELEASE -> clearRightClickDown();
            case VEvents.Widget.MIDDLE_CLICK_RELEASE -> clearMiddleClickDown();

            case VEvents.Widget.HOVER_LEAVE -> {
                clearLeftClickDown();
                clearRightClickDown();
                clearMiddleClickDown();
            }

            case VEvents.Animation.FINISH -> {
                CompiledAnimation animation = (CompiledAnimation) args[0];
                if (animation.name.equals(VAnimation.INTERNAL_TRANSITION_NAME)) {
                    transitionOrigin = null;
                    isTransitionUnwinding = false;
                }
            }
            case VEvents.Animation.UNWIND_BEGIN -> isTransitionUnwinding = true;
            case VEvents.Animation.REWIND_BEGIN -> isTransitionUnwinding = false;
        }
    }

    @Override
    public void afterBuiltinEvent(String name, Object... args) {
        updateIfNeeded();
    }

    private void updateIfNeeded() {
        StyleState state = createStyleState();
        if (state != handledPrevStyleState) {
            update();
            handledPrevStyleState = state;
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

    public record RenderContext(
            int x, int y,
            int width, int height,
            float rotation, float scale,
            boolean hasTransparency
    ) {}
}
