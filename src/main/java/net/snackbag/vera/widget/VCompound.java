package net.snackbag.vera.widget;

import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.EventHandler;
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

    public VCompound(int x, int y, int width, int height, VLayout layout, VAppAccess app) {
        super(x, y, width, height, app);

        this.layout = layout;

        move(x, y);
        setSize(width, height);

        init();
    }

    public abstract void init();

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
        if (widgets.contains(widget)) {
            MinecraftVera.LOGGER.error("Can't add widget %s to compound %s, because it is already added"
                    .formatted(widget.toString(), getClass().getSimpleName()));
            return;
        }

        widget.classes.add(identifier.toString());
        widgets.add(widget);
        layout.addElement(widget);
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

    //
    // Delegation
    //

    @Override
    public @Nullable VDelegator getDelegator() {
        return this;
    }

    }

    //
    // Spoofing widget positions
    //

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        layout.move(x, y);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        layout.setSize(width, height);
    }

    //
    // Rendering
    //
    @Override
    public void renderSelf() {
        beforeRender();
        animations.updateLifetimes();

        if (visibilityConditionsPassed()) {
            VRenderContext ctx = createRenderContext();
            Vera.renderer.pushContext(ctx);

            renderContent(ctx);

            MCVeraRenderer.drawContext.getMatrices().translate(-getX(), -getY(), 0);
            for (VWidget<?> widget : widgets) widget.renderSelf();

            renderBorder(ctx);
            renderOverlay(ctx);

            Vera.renderer.popContext();
        }

        afterRender();
    }
}
