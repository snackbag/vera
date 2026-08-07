package net.snackbag.vera;

import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.EventHandler;
import net.snackbag.vera.event.VElementEvent;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.layout.VLayout;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class VElement {
    protected int _x;
    protected int _y;
    protected int width;
    protected int height;

    public boolean visible = true;

    public final EventHandler events;
    public final VAppAccess appAccess;
    private final List<Supplier<Boolean>> visibilityConditions = new ArrayList<>();

    protected @Nullable VLayout layout;

    public VElement(VAppAccess app, int x, int y, int width, int height) {
        this.appAccess = app;

        this.events = new EventHandler(this);
        this.events.preprocessor = this::handleBuiltinEvent;
        this.events.postprocessor = this::afterBuiltinEvent;

        addVisibilityCondition(() -> visible);

        this._x = x;
        this._y = y;
        this.width = width;
        this.height = height;

        onLayoutSwap(ctx -> this.layout = ctx.layout()); // event gets called. we use the event itself to change the layout
        onLayoutRemove(() -> this.layout = null);
    }

    public VeraApp getApp() {
        return appAccess.get();
    }

    public void handleBuiltinEvent(String name, VEventContext ctx) {}
    public void afterBuiltinEvent(String name, VEventContext ctx) {}

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

    public void onMessage(Consumer<VElementEvent.Message> ctx) {
        events.register(VEvents.Element.MESSAGE, ctx);
    }

    public void sendMessage(VElement element, String type, @Nullable Object content) {
        element.events.fire(new VElementEvent.Message(this, type, content));
    }

    public void onLayoutSwap(Consumer<VElementEvent.LayoutSwap> ctx) {
        events.register(VEvents.Element.LAYOUT_SWAP, ctx);
    }

    public void onLayoutRemove(Runnable executor) {
        events.register(VEvents.Element.LAYOUT_REMOVE, executor);
    }

    //
    // Position & Size
    //

    public int getX() {
        return layout != null ? layout.posOf(this).x : _x;
    }

    public int getY() {
        return layout != null ? layout.posOf(this).y : _y;
    }

    public final int getRawX() {
        return _x;
    }

    public final int getRawY() {
        return _y;
    }

    @Deprecated(forRemoval = true)
    public int getEffectiveX() {
        return getX();
    }

    @Deprecated(forRemoval = true)
    public int getEffectiveY() {
        return getY();
    }

    public int getRelativeMouseX() {
        return Vera.getMouseX() - getX() - getApp().getX();
    }

    public int getRelativeMouseY() {
        return Vera.getMouseY() - getY() - getApp().getY();
    }

    public void move(int both) {
        move(both, both);
    }

    public void move(int x, int y) {
        this._x = x;
        this._y = y;
    }

    /**
     * Width of the inner content of an element. Styles like padding or border size should be added
     * through {@link #getEffectiveWidth()}.
     *
     * @return the inner width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Height of the inner content of an element. Styles like padding or border size should be added
     * through {@link #getEffectiveHeight()}.
     *
     * @return the inner height
     */
    public int getHeight() {
        return height;
    }

    /**
     * The element's content width plus styles like padding or border size.
     *
     * @return the full width
     */
    public int getEffectiveWidth() {
        return getWidth();
    }

    /**
     * The element's content height plus styles like padding or border size.
     *
     * @return the full size
     */
    public int getEffectiveHeight() {
        return getHeight();
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

    public <T extends VElement> T alsoAddTo(VLayout layout) {
        layout.addElement(this);
        return (T) this;
    }
}
