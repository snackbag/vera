package net.snackbag.vera.core;

import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.widget.VWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class VeraApp {
    private final VeraProvider provider;
    private final List<VWidget> widgets;
    private VColor backgroundColor;

    private int x;
    private int y;
    private int width;
    private int height;

    private boolean visible;

    public VeraApp(VeraProvider provider) {
        this.provider = provider;
        this.widgets = new ArrayList<>();
        this.backgroundColor = VColor.white();

        provider.handleAppInitialization(this);
        this.width = Math.min(provider.getScreenWidth(), 400);
        this.height = Math.min(provider.getScreenHeight(), 300);

        this.x = (provider.getScreenHeight() - this.width) / 2;
        this.y = (provider.getScreenHeight() - this.width) / 2;

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
}
