package net.snackbag.vera;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.EventHandler;
import net.snackbag.vera.event.VWidgetMessageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class VElement {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public boolean visible;

    public final EventHandler events;
    public final VeraApp app;
    private final List<Supplier<Boolean>> visibilityConditions = new ArrayList<>();

    public VElement(VeraApp app, int x, int y, int width, int height) {
        this.app = app;

        this.events = new EventHandler(this);
        this.events.preprocessor = this::handleBuiltinEvent;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render();
    public void handleBuiltinEvent(String name, Object... args) {}

    //
    // Visibility
    //

    public boolean visibilityConditionsPassed() {
        return visibilityConditions.parallelStream().allMatch(Supplier::get);
    }

    public void addVisibilityCondition(Supplier<Boolean> condition) {
        visibilityConditions.add(condition);
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    //
    // Events
    //

    public void onMessage(VWidgetMessageEvent executor) {
        events.register("elem-message", args -> executor.run((VWidgetMessageEvent.Context) args[0]));
    }

    public void sendMessage(VElement element, String type, @Nullable Object content) {
        events.fireEvent("elem-message", new VWidgetMessageEvent.Context(this, type, content));
    }

    //
    // Position & Size
    //

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int both) {
        move(both, both);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        setSize(width, height);
    }

    public void setHeight(int height) {
        setSize(width, height);
    }

    public void setSize(int both) {
        setSize(both, both);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
