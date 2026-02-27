package net.snackbag.vera.core;

import net.minecraft.client.MinecraftClient;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.flag.VAppFlag;
import net.snackbag.vera.flag.VAppPositioningFlag;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.util.VGeometry;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public abstract class VeraApp implements VAppAccess {
    public final VStyleSheet styleSheet = new VStyleSheet();

    private final List<VWidget<?>> widgets;
    private final HashMap<String, VShortcut> shortcuts;
    private VColor backgroundColor;
    private VCursorShape cursorShape;
    private boolean cursorVisible;

    private int x;
    private int y;
    private int width;
    private int height;

    private boolean visible;
    private @Nullable VWidget<?> focusedWidget;
    private VAppPositioningFlag positioning;

    public VeraApp() {
        this(true);
    }

    public VeraApp(boolean mouseRequired) {
        this.widgets = new ArrayList<>();
        this.shortcuts = new HashMap<>();
        this.backgroundColor = VColor.transparent();
        this.cursorShape = VCursorShape.DEFAULT;
        this.cursorVisible = true;
        if (mouseRequired) setFlag(VAppFlag.REQUIRES_MOUSE, true);

        Vera.provider.handleAppInitialization(this);

        this.width = Vera.provider.getScreenWidth();
        this.height = Vera.provider.getScreenHeight();
        this.x = 0;
        this.y = 0;

        this.visible = false;
        setPositioning(VAppPositioningFlag.SCREEN);
    }

    @Override
    public @NotNull VeraApp get() {
        return this;
    }

    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;

        if (!visible || !hasFlag(VAppFlag.REQUIRES_MOUSE)) return;
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
        if (!visible || !hasFlag(VAppFlag.REQUIRES_MOUSE)) return;
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

    public void setSize(int both) {
        setSize(both, both);
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
        this.move(both, both);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveToHierarchyTop() {
        if (!hasFlag(VAppFlag.HIERARCHIC)) {
            MinecraftVera.LOGGER.warn("Failed to move app to top, because hierarchy isn't enabled");
            return;
        }

        MCVeraData.appFlags.get(VAppFlag.HIERARCHIC).remove(this);
        MCVeraData.appFlags.get(VAppFlag.HIERARCHIC).add(0, this);
    }

    public abstract void init();

    @Override
    public List<VWidget<?>> getWidgets() {
        return new ArrayList<>(widgets);
    }

    @Override
    public void addWidget(VWidget<?> widget) {
        if (widgets.contains(widget)) {
            MinecraftVera.LOGGER.error("Can't add widget %s to app %s, because it is already added"
                    .formatted(widget.toString(), getClass().getSimpleName()));
            return;
        }

        this.widgets.add(widget);
    }

    @Override
    public void removeWidget(VWidget<?> widget) {
        if (!widgets.contains(widget)) return;

        if (isFocusedWidget(widget)) setFocusedWidget(null);
        if (widget.isLeftClickDown()) widget.events.fire(VEvents.Widget.LEFT_CLICK_RELEASE);
        if (widget.isMiddleClickDown()) widget.events.fire(VEvents.Widget.MIDDLE_CLICK_RELEASE);
        if (widget.isRightClickDown()) widget.events.fire(VEvents.Widget.RIGHT_CLICK_RELEASE);
        if (widget.isHovered()) widget.events.fire(VEvents.Widget.HOVER_LEAVE);

        this.widgets.remove(widget);
    }

    public void setBackgroundColor(VColor color) {
        this.backgroundColor = color;
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void render() {
        Vera.renderer.drawRect(x, y, width, height, backgroundColor);
    }

    public void renderAfterWidgets() {}

    public void renderHierarchyOverlay() {
        Vera.renderer.drawRect(x, y, width, height,
                backgroundColor.isVisible()
                        ? VColor.black().withOpacity(0.2f)
                        : backgroundColor.sub(40).withOpacity(0.2f)
        );
    }

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

        int widgetX = widget.getHitboxX();
        int widgetY = widget.getHitboxY();
        int widgetWidth = widget.getHitboxWidth();
        int widgetHeight = widget.getHitboxHeight();
        return VGeometry.isInBox(px, py, widgetX, widgetY, widgetWidth, widgetHeight);
    }

    public boolean isPointOverThis(int px, int py) {
        if (!isVisible()) return false;

        return VGeometry.isInBox(px, py, x, y, width, height);
    }

    public void setFocusedWidget(@Nullable VWidget<?> widget) {
        if (this.focusedWidget != widget) {
            VWidget<?> oldWidget = this.focusedWidget;
            this.focusedWidget = widget;

            if (oldWidget != null) oldWidget.events.fire(VEvents.Widget.FOCUS_STATE_CHANGE);
            if (widget != null) widget.events.fire(VEvents.Widget.FOCUS_STATE_CHANGE);
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

    public VAppPositioningFlag getPositioning() {
        return positioning;
    }

    public void setPositioning(VAppPositioningFlag positioning) {
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

    public boolean hasFlag(VAppFlag flag) {
        if (!MCVeraData.appFlags.containsKey(flag)) return false;
        else return MCVeraData.appFlags.get(flag).contains(this);
    }

    public void setFlag(VAppFlag flag, boolean enabled) {
        if (enabled == hasFlag(flag)) return; // if nothing has to be changed, change nothing

        if (!enabled) MCVeraData.appFlags.get(flag).remove(this);
        else {
            if (!MCVeraData.appFlags.containsKey(flag)) MCVeraData.appFlags.put(flag, new ArrayList<>());
            MCVeraData.appFlags.get(flag).add(this);
        }

        // handle mouse requirements
        if (flag == VAppFlag.REQUIRES_MOUSE) {
            Vera.provider.handleAppSetMouseRequired(this, enabled);

            if (!visible || !enabled) return;
            GLFW.glfwSetInputMode(
                    MinecraftClient.getInstance().getWindow().getHandle(),
                    GLFW.GLFW_CURSOR,
                    cursorVisible ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_HIDDEN);
        }
    }
}
