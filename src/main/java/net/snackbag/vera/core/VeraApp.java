package net.snackbag.vera.core;

import net.minecraft.client.MinecraftClient;
import net.snackbag.vera.Vera;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class VeraApp {
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
    private @Nullable VWidget<?> focusedWidget;

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

    public abstract void init();

    public List<VWidget<?>> getWidgets() {
        return new ArrayList<>(widgets);
    }

    public void addWidget(VWidget<?> widget) {
        this.widgets.add(widget);
    }

    public void removeWidget(VWidget<?> widget) {
        if (!widgets.contains(widget)) return;

        if (isFocusedWidget(widget)) setFocusedWidget(null);
        if (widget.isLeftClickDown()) widget.fireEvent("left-click-release");
        if (widget.isMiddleClickDown()) widget.fireEvent("middle-click-release");
        if (widget.isRightClickDown()) widget.fireEvent("right-click-release");
        if (widget.isHovered()) widget.fireEvent("hover-leave");

        this.widgets.remove(widget);
    }

    public void setBackgroundColor(VColor color) {
        this.backgroundColor = color;
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void render() {
        Vera.renderer.drawRect(this, x, y, width, height, 0, backgroundColor);
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

    public List<VWidget<?>> getHoveredWidgets() {
        return getHoveredWidgets(Vera.provider.getMouseX(), Vera.provider.getMouseY());
    }

    public List<VWidget<?>> getHoveredWidgets(int mouseX, int mouseY) {
        return getWidgets().parallelStream()
                .filter(widget -> isMouseOverWidget(widget, mouseX, mouseY))
                .filter(VWidget::visibilityConditionsPassed)
                .collect(Collectors.toList());
    }

    private boolean isMouseOverWidget(VWidget<?> widget, int mouseX, int mouseY) {
        if (!widget.visibilityConditionsPassed()) return false;

        int widgetX = widget.getHitboxX() + x;
        int widgetY = widget.getHitboxY() + y;
        int widgetWidth = widget.getHitboxWidth();
        int widgetHeight = widget.getHitboxHeight();
        return mouseX >= widgetX && mouseX <= widgetX + widgetWidth &&
                mouseY >= widgetY && mouseY <= widgetY + widgetHeight;
    }

    public boolean isMouseOverApp(int mouseX, int mouseY) {
        if (!isVisible()) return false;
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void setFocusedWidget(@Nullable VWidget<?> widget) {
        if (this.focusedWidget != widget) {
            VWidget<?> oldWidget = this.focusedWidget;
            this.focusedWidget = widget;

            if (oldWidget != null) oldWidget.fireEvent("focus-state-change");
            if (widget != null) widget.fireEvent("focus-state-change");
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

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (hasFocusedWidget()) getFocusedWidget().keyPressed(keyCode, scanCode, modifiers);
    }

    public void charTyped(char chr, int modifiers) {
        if (hasFocusedWidget()) getFocusedWidget().charTyped(chr, modifiers);
    }
}
