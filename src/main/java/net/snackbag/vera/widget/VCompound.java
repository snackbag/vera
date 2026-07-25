package net.snackbag.vera.widget;

import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.EventHandler;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.layout.VLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class VCompound<T extends VWidget<T>> extends VWidget<T> implements VAppAccess, VDelegator {
    protected final VLayout layout;
    private final List<VWidget<?>> widgets = new ArrayList<>();
    public final UUID identifier = UUID.randomUUID();

    private final EventHandler delegatedEvents;

    public VCompound(VAppAccess app, VLayout layout, int x, int y, int width, int height) {
        super(app, x, y, width, height);

        this.delegatedEvents = new EventHandler(this);
        this.delegatedEvents.preprocessor = this::handleDelegatedEvent;

        this.layout = layout;

        move(x, y);
        setSize(width, height);
    }

    //
    // App Access
    //

    @Override
    public @NotNull VeraApp get() {
        return getApp();
    }

    @Override
    public List<VWidget<?>> getWidgets() {
        return new ArrayList<>(widgets);
    }

    @Override
    public void addWidget(VWidget<?> widget) {
        addWidget(widget, true);
    }

    protected void addWidget(VWidget<?> widget, boolean addToLayout) {
        if (widgets.contains(widget)) {
            MinecraftVera.LOGGER.error("Can't add widget %s to compound %s, because it is already added"
                    .formatted(widget.toString(), getClass().getSimpleName()));
            return;
        }

        widget.classes.add(identifier.toString());
        widgets.add(widget);
        if (addToLayout) layout.addElement(widget);

        appAccess.get().addWidget(widget);
    }

    @Override
    public void removeWidget(VWidget<?> widget) {
        if (!widgets.contains(widget)) {
            MinecraftVera.LOGGER.error("Can't remove widget %s from compound %s, because it wasn't added"
                    .formatted(widget.toString(), getClass().getSimpleName()));
            return;
        }

        widget.classes.add(identifier.toString());
        layout.removeElement(widget);
        widgets.remove(widget);
    }

    protected @Nullable VWidget<?> getHoveredWidget() {
        return widgets.stream()
                .filter(VWidget::isHovered)
                .findFirst().orElse(null);
    }

    //
    // Delegation
    //

    @Override
    public @Nullable VDelegator getDelegator() {
        return this;
    }

    @Override
    public boolean isDelegatedPointOver(int px, int py, VWidget<?> widget) {
        return true;
    }

    @Override
    public EventHandler getDelegatedEventHandler() {
        return delegatedEvents;
    }

    protected void handleDelegatedEvent(String event, VEventContext ctx) {
        switch (event) {
            case VEvents.Widget.HOVER -> setHovered(true);
            case VEvents.Widget.HOVER_LEAVE -> setHovered(false);
        }
    }

    @Override
    public void setHovered(boolean hovered) {
        for (VWidget<?> widget : getWidgets()) {
            if (widget.isHovered()) return;
        }

        super.setHovered(hovered);
    }

    //
    // Spoofing widget positions
    //

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        updateLayoutPosition(x, y);
    }

    protected void updateLayoutPosition(int x, int y) {
        layout.move(x, y);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        updateLayoutSize(width, height);
    }

    protected void updateLayoutSize(int width, int height) {
        layout.setSize(width, height);
    }

    @Override
    public boolean isPointOverThis(int px, int py) {
        if (!super.isPointOverThis(px, py)) return false;

        for (VWidget<?> child : widgets) {
            if (child.isPointOverThis(px, py)) return false;
        }

        return true;
    }

    //
    // Rendering
    //
    public void renderChildren(VRenderContext ctx) {
        Vera.renderer.renderCompoundChildren(this);
    }

    public void renderAfterChildren(VRenderContext ctx) {}

    @Override
    public void renderSelf() {
        beforeRender();
        animations.updateLifetimes();

        if (visibilityConditionsPassed()) {
            VRenderContext ctx = createRenderContext();
            Vera.renderer.pushContext(ctx);

            renderContent(ctx);
            renderChildren(ctx);
            renderAfterChildren(ctx);

            renderBorder(ctx);
            renderOverlay(ctx);

            Vera.renderer.popContext();
        }

        afterRender();
    }
}
