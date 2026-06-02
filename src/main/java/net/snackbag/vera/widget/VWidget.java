package net.snackbag.vera.widget;

import net.snackbag.vera.VElement;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.core.v4.V4Color;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.event.*;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.style.VEffectState;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.CompiledAnimation;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.util.DragHandler;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.util.VGeometry;

import java.util.*;
import java.util.function.Consumer;

public abstract class VWidget<T extends VWidget<T>> extends VElement {
    public AnimationEngine animations = new AnimationEngine(this);
    protected boolean hasTransparency = false;

    public boolean focusOnClick = true;
    private boolean hovered = false;

    private boolean leftClickDown = false;
    private boolean middleClickDown = false;
    private boolean rightClickDown = false;
    private VStyleState handledPrevStyleState = VEffectState.DEFAULT.asStyleState(); // constantly updates
    private VStyleState prevStyleState = VEffectState.DEFAULT.asStyleState(); // updates max once per frame, can be seen as the definite result

    private VStyleState transitionOrigin = null;
    private boolean isTransitionUnwinding = false;

    public final LinkedHashSet<String> classes = new LinkedHashSet<>();

    public VWidget(int x, int y, int width, int height, VAppAccess app) {
        super(app, x, y, width, height);
    }

    public abstract void renderContent(VRenderContext ctx);

    public int getHitboxX() {
        return getX();
    }

    public int getHitboxY() {
        return getY();
    }

    public int getHitboxWidth() {
        return getEffectiveWidth();
    }

    public int getHitboxHeight() {
        return getEffectiveHeight();
    }

    @SuppressWarnings("unchecked")
    public <V> void setStyle(String key, V... value) {
        getApp().styleSheet.setKey(this, key, value);
    }

    @SuppressWarnings("unchecked")
    public <V> void setStyle(String key, VEffectState state, V... value) {
        getApp().styleSheet.setKey(this, key, value, state);
    }

    public <V> V getStyle(String key) {
        return animations.animateStyle(key, getApp().styleSheet.getKey(this, key));
    }

    public <V> V getStyle(String key, VEffectState state) {
        return animations.animateStyle(key, getApp().styleSheet.getKey(this, key, state));
    }

    public <V> V getStyle(String key, VStyleState state) {
        return animations.animateStyle(key, getApp().styleSheet.getKey(this, key, state));
    }

    public <V> V getStyleOrDefault(String key, V dflt) {
        V style = getStyle(key);
        return style != null ? style : dflt;
    }

    public <V> V getStyleOrDefault(String key, V dflt, VEffectState state) {
        V style = getStyle(key, state);
        return style != null ? style : dflt;
    }

    public <V> V getStyleOrDefault(String key, V dflt, VStyleState state) {
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

    public VStyleState createStyleState() {
        return createEffectState().asStyleState();
    }

    private VEffectState createEffectState() {
        // Clicks first
        if (leftClickDown) return VEffectState.LEFT_CLICKED;
        else if (middleClickDown) return VEffectState.MIDDLE_CLICKED;
        else if (rightClickDown) return VEffectState.RIGHT_CLICKED;

        else if (DragHandler.isDragging() && DragHandler.target == this) {
            return switch (DragHandler.button) {
                case LEFT -> VEffectState.LC_DRAGGING;
                case MIDDLE -> VEffectState.MC_DRAGGING;
                case RIGHT -> VEffectState.RC_DRAGGING;
            };
        }

        // Hover as last, since everything else is hover too
        else if (isHovered()) return VEffectState.HOVERED;
        else return VEffectState.DEFAULT;
    }

    public VRenderContext createRenderContext() {
        VStyleState state = createStyleState();
        VeraApp app = getApp();
        return new VRenderContext(
                app.getX() + getX(), app.getY() + getY(),
                getEffectiveWidth(), getEffectiveHeight(),
                getStyle("rotation", state), getStyle("scale", state),
                hasTransparency
        );
    }

    public void renderSelf() {
        beforeRender();
        animations.updateLifetimes();

        if (visibilityConditionsPassed()) {
            VRenderContext ctx = createRenderContext();
            Vera.renderer.pushContext(ctx);

            renderContent(ctx);
            renderBorder(ctx);
            renderOverlay(ctx);

            Vera.renderer.ensureClearContext(ctx);
            Vera.renderer.popContext();
        }

        afterRender();
    }

    public void renderBorder(VRenderContext ctx) {
        // TODO: [Render Rework] Better border rendering

        var state = createStyleState();

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

    public void renderOverlay(VRenderContext ctx) {
        var state = createStyleState();

        Vera.renderer.drawRect(ctx, 0, 0, getEffectiveWidth(), getEffectiveHeight(), getStyle("overlay", state));
    }

    public void beforeRender() {
        VeraApp app = getApp();
        var state = createStyleState();

        if (!state.equals(prevStyleState)) {
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

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
        events.fire(new VWidgetEvent.TransparencyStateChanged(hasTransparency));
    }

    public void update() {
        var state = createStyleState();

        getApp().setCursorShape(getStyle("cursor", state));
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

    public void onMouseScroll(Consumer<VWidgetEvent.MouseScroll> ctx) {
        events.register(VEvents.Widget.SCROLL, ctx);
    }

    public void onMouseMove(Consumer<VWidgetEvent.MouseMove> ctx) {
        events.register(VEvents.Widget.MOUSE_MOVE, ctx);
    }

    public void onMouseDrag(Consumer<VWidgetEvent.MouseDrag> ctx) {
        events.register(VEvents.Widget.MOUSE_DRAG, ctx);
    }

    public void onFocusStateChange(Runnable runnable) {
        events.register(VEvents.Widget.FOCUS_STATE_CHANGE, runnable);
    }

    public void onFilesDropped(Consumer<VWidgetEvent.FilesDropped> ctx) {
        events.register(VEvents.Widget.FILES_DROPPED, ctx);
    }

    public void onAnimationBegin(Consumer<VAnimationEvent.Begin> ctx) {
        events.register(VEvents.Animation.BEGIN, ctx);
    }

    public void onAnimationUnwindBegin(Consumer<VAnimationEvent.Unwind> ctx) {
        events.register(VEvents.Animation.UNWIND_BEGIN, ctx);
    }

    public void onAnimationRewindBegin(Consumer<VAnimationEvent.Rewind> ctx) {
        events.register(VEvents.Animation.REWIND_BEGIN, ctx);
    }

    public void onAnimationFinish(Consumer<VAnimationEvent.Finish> ctx) {
        events.register(VEvents.Animation.FINISH, ctx);
    }

    public void onTransparencyStateChanged(Consumer<VWidgetEvent.TransparencyStateChanged> ctx) {
        events.register(VEvents.Widget.TRANSPARENCY_STATE_CHANGED, ctx);
    }

    public boolean isPointOverThis(int px, int py) {
        if (!visibilityConditionsPassed()) return false;

        int widgetX = getHitboxX();
        int widgetY = getHitboxY();
        int widgetWidth = getHitboxWidth();
        int widgetHeight = getHitboxHeight();
        return VGeometry.isInBox(px, py, widgetX, widgetY, widgetWidth, widgetHeight);
    }

    @Override
    public void handleBuiltinEvent(String event, VEventContext ctx) {
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
                CompiledAnimation animation = ((VAnimationEvent.Finish) ctx).animation();
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
    public void afterBuiltinEvent(String name, VEventContext ctx) {
        updateIfNeeded();
    }

    private void updateIfNeeded() {
        var state = createStyleState();
        if (!state.equals(handledPrevStyleState)) {
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
        return getApp().isFocusedWidget(this);
    }

    public void setFocused(boolean focused) {
        VeraApp app = getApp();

        if (focused) app.setFocusedWidget(this);
        else app.setFocusedWidget(null);
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public void charTyped(char chr, int modifiers) {}

    public void remove() {
        appAccess.removeWidget(this);
    }

    public T alsoAddClass(String clazz) {
        classes.add(clazz);
        return (T) this;
    }

    public T alsoAdd() {
        appAccess.addWidget(this);
        return (T) this;
    }

    @Override
    public T alsoAddTo(VLayout layout) {
        super.alsoAddTo(layout);
        alsoAdd();

        return (T) this;
    }
}
