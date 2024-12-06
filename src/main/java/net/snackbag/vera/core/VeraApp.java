package net.snackbag.vera.core;

import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class VeraApp {
    private final VeraProvider provider;
    private final List<VWidget> widgets;
    private final HashMap<String, VShortcut> shortcuts;
    private VColor backgroundColor;

    private int x;
    private int y;
    private int width;
    private int height;

    private boolean visible;

    public VeraApp(VeraProvider provider) {
        this.provider = provider;
        this.widgets = new ArrayList<>();
        this.shortcuts = new HashMap<>();
        this.backgroundColor = VColor.white();

        provider.handleAppInitialization(this);

        this.width = provider.getScreenWidth();
        this.height = provider.getScreenHeight();
        this.x = 0;
        this.y = 0;

        this.visible = false;
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
        this.visible = visible;
        provider.handleAppVisibilityChange(this, visible);
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

    public VeraProvider getProvider() {
        return provider;
    }

    public abstract void init();

    public List<VWidget> getWidgets() {
        return new ArrayList<>(widgets);
    }

    public void addWidget(VWidget widget) {
        this.widgets.add(widget);
    }

    public void setBackgroundColor(VColor color) {
        this.backgroundColor = color;
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void render() {
        VeraRenderer renderer = provider.getRenderer();

        renderer.drawRect(this, x, y, width, height, 0, backgroundColor);
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

    public List<VWidget> getHoveredWidgets() {
        return getHoveredWidgets(provider.getMouseX(), provider.getMouseY());
    }

    public List<VWidget> getHoveredWidgets(int mouseX, int mouseY) {
        return widgets.parallelStream()
                .filter(widget -> isMouseOverWidget(widget, mouseX, mouseY))
                .collect(Collectors.toList());
    }

    private boolean isMouseOverWidget(VWidget widget, int mouseX, int mouseY) {
        if (!widget.isVisible()) return false;

        int widgetX = widget.getRealX() + x;
        int widgetY = widget.getRealY() + y;
        int widgetWidth = widget.getFullWidth();
        int widgetHeight = widget.getFullHeight();
        return mouseX >= widgetX && mouseX <= widgetX + widgetWidth &&
                mouseY >= widgetY && mouseY <= widgetY + widgetHeight;
    }
}
