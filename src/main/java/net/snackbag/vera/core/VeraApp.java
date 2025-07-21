package net.snackbag.vera.core;

import net.minecraft.client.MinecraftClient;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.VeraPipeline;
import net.snackbag.vera.style.animation.composite.AnimationComposite;
import net.snackbag.vera.style.animation.composite.UnwindComposite;
import net.snackbag.vera.util.Geometry;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public abstract class VeraApp {
    public final VStyleSheet styleSheet = new VStyleSheet();
    public final VeraPipeline pipeline = new VeraPipeline(this);

    private final List<VWidget<?>> widgets;
    private final HashMap<String, VShortcut> shortcuts;
    private VColor backgroundColor;
    private VCursorShape cursorShape;
    private boolean cursorVisible;
    private boolean mouseRequired;

    private int x;
    private int y;
    private int width;
    private int height;

    private boolean visible;
    private boolean requiresHierarchy;
    private @Nullable VWidget<?> focusedWidget;
    private VWindowPositioningFlag positioning;

    public VeraApp() {
        this(true);
    }

    public VeraApp(boolean mouseRequired) {
        this.widgets = new ArrayList<>();
        this.shortcuts = new HashMap<>();
        this.backgroundColor = VColor.transparent();
        this.cursorShape = VCursorShape.DEFAULT;
        this.cursorVisible = true;
        this.mouseRequired = mouseRequired;

        Vera.provider.handleAppInitialization(this);

        this.width = Vera.provider.getScreenWidth();
        this.height = Vera.provider.getScreenHeight();
        this.x = 0;
        this.y = 0;

        this.visible = false;
        setPositioning(VWindowPositioningFlag.SCREEN);

        loadComposites();
    }

    public void loadComposites() {
        pipeline.addPass(new AnimationComposite());
        pipeline.addPass(new UnwindComposite());
    }

    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;

        if (!visible || !mouseRequired) return;
        GLFW.glfwSetInputMode(
                MinecraftClient.getInstance().getWindow().getHandle(),
                GLFW.GLFW_CURSOR,
                cursorVisible ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_HIDDEN);
    }

    public void hideCursor() {
        setCursorVisible(false);
    }

    public void showCursor() {
        setCursorVisible(true);
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    public boolean isMouseRequired() {
        return mouseRequired;
    }

    public void setMouseRequired(boolean mouseRequired) {
        if (this.mouseRequired == mouseRequired) return;

        Vera.provider.handleAppSetMouseRequired(this, mouseRequired);
        this.mouseRequired = mouseRequired;

        if (!visible || !mouseRequired) return;
        GLFW.glfwSetInputMode(
                MinecraftClient.getInstance().getWindow().getHandle(),
                GLFW.GLFW_CURSOR,
                cursorVisible ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_HIDDEN);
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isShown() {
        return visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    public void show() {
        setVisibility(true);
    }

    public void hide() {
        setVisibility(false);
    }

    public void setVisibility(boolean visible) {
        Vera.provider.handleAppVisibilityChange(this, visible);
        this.visible = visible;

        if (visible) setCursorShape(cursorShape);
        if (!visible || !mouseRequired) return;
        GLFW.glfwSetInputMode(
                MinecraftClient.getInstance().getWindow().getHandle(),
                GLFW.GLFW_CURSOR,
                cursorVisible ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_HIDDEN);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int both) {
        this.move(both, both);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setRequiresHierarchy(boolean requires) {
        if (MCVeraData.appHierarchy.contains(this) && !requires) {
            MCVeraData.appHierarchy.remove(this);
        }

        MCVeraData.appHierarchy.add(this);
        this.requiresHierarchy = requires;
    }

    public void moveToHierarchyTop() {
        if (!requiresHierarchy) return;

        MCVeraData.appHierarchy.remove(this);
        MCVeraData.appHierarchy.add(0, this);
    }

    public boolean isRequiresHierarchy() {
        return requiresHierarchy;
    }

    public abstract void init();

    public List<VWidget<?>> getWidgets() {
        return new ArrayList<>(widgets);
    }

    public List<VWidget<?>> getWidgetsReversed() {
        List<VWidget<?>> widgets = getWidgets();
        Collections.reverse(widgets);

        return widgets;
    }

    public void addWidget(VWidget<?> widget) {
        if (widgets.contains(widget)) return;
        this.widgets.add(widget);
    }

    public void removeWidget(VWidget<?> widget) {
        if (!widgets.contains(widget)) return;

        if (isFocusedWidget(widget)) setFocusedWidget(null);
        if (widget.isLeftClickDown()) widget.events.fire("left-click-release");
        if (widget.isMiddleClickDown()) widget.events.fire("middle-click-release");
        if (widget.isRightClickDown()) widget.events.fire("right-click-release");
        if (widget.isHovered()) widget.events.fire("hover-leave");

        this.widgets.remove(widget);
    }

    public void setBackgroundColor(VColor color) {
        this.backgroundColor = color;
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void render() {
        Vera.renderer.drawRect(this, 0, 0, width, height, 0, backgroundColor);
    }

    public void renderAfterWidgets() {}

    public void update() {}

    public void addShortcut(VShortcut shortcut) {
        shortcuts.put(shortcut.getCombination(), shortcut);
    }

    public void handleShortcut(String combination) {
        Set<String> combinations = shortcuts.keySet();
        combination = combination.toLowerCase().replace(" ", "");

        for (String combi : combinations) {
            if (combi.equals(combination)) shortcuts.get(combi).run();
        }
    }

    public List<VShortcut> getShortcuts() {
        return List.copyOf(shortcuts.values());
    }

    public @Nullable VWidget<?> getTopWidgetAt(int px, int py) {
        int mx = px - x;
        int my = py - y;

        return getWidgetsReversed().stream()
                .filter(widget -> isPointOverWidget(widget, mx, my))
                .filter(VWidget::visibilityConditionsPassed)
                .findFirst().orElse(null);
    }

    private boolean isPointOverWidget(VWidget<?> widget, int px, int py) {
        if (!widget.visibilityConditionsPassed()) return false;

        int widgetX = widget.getHitboxX() + x;
        int widgetY = widget.getHitboxY() + y;
        int widgetWidth = widget.getHitboxWidth();
        int widgetHeight = widget.getHitboxHeight();
        return Geometry.isInBox(px, py, widgetX, widgetY, widgetWidth, widgetHeight);
    }

    public boolean isPointOverThis(int px, int py) {
        if (!isVisible()) return false;

        return Geometry.isInBox(px, py, x, y, width, height);
    }

    public void setFocusedWidget(@Nullable VWidget<?> widget) {
        if (this.focusedWidget != widget) {
            VWidget<?> oldWidget = this.focusedWidget;
            this.focusedWidget = widget;

            if (oldWidget != null) oldWidget.events.fire("focus-state-change");
            if (widget != null) widget.events.fire("focus-state-change");
        }
    }

    public @Nullable VWidget<?> getFocusedWidget() {
        return focusedWidget;
    }

    public boolean isFocusedWidget(VWidget<?> widget) {
        return focusedWidget != null && focusedWidget == widget;
    }

    public boolean hasFocusedWidget() {
        return focusedWidget != null;
    }

    public VCursorShape getCursorShape() {
        return cursorShape;
    }

    public void setCursorShape(VCursorShape cursorShape) {
        this.cursorShape = cursorShape;

        if (!isVisible()) return;
        GLFW.glfwSetCursor(
                MinecraftClient.getInstance().getWindow().getHandle(),
                cursorShape.getGLFWCursor()
        );
    }

    public VWindowPositioningFlag getPositioning() {
        return positioning;
    }

    public void setPositioning(VWindowPositioningFlag positioning) {
        // make sure hashmaps exist
        if (!MCVeraData.visibleApplications.containsKey(this.positioning))
            MCVeraData.visibleApplications.put(this.positioning, new LinkedHashSet<>());
        if (!MCVeraData.visibleApplications.containsKey(positioning))
            MCVeraData.visibleApplications.put(positioning, new LinkedHashSet<>());

        // if visible, then we can also add the app itself
        if (isVisible()) {
            MCVeraData.visibleApplications.get(positioning).add(this);
        }

        // doesn't matter if visible or not, we always remove it from its original
        MCVeraData.visibleApplications.get(this.positioning).remove(this);
        this.positioning = positioning;
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (hasFocusedWidget()) getFocusedWidget().keyPressed(keyCode, scanCode, modifiers);
    }

    public void charTyped(char chr, int modifiers) {
        if (hasFocusedWidget()) getFocusedWidget().charTyped(chr, modifiers);
    }

    public void mergeStyleSheet(VStyleSheet target) {
        styleSheet.addSheet(target);
    }
}
