package net.snackbag.vera.widget;

import net.minecraft.util.math.MathHelper;
import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VWidgetEvent;
import net.snackbag.vera.layout.VLayout;
import org.jetbrains.annotations.Nullable;

public class VScrollBox extends VCompound<VScrollBox> {
    public int deltaYPerScroll = 10;
    public int deltaHPerScroll = 10;

    private double scrollX = 0;
    private double scrollY = 0;
    private @Nullable Integer maxScrollX = null;
    private @Nullable Integer maxScrollY = null;

    public VScrollBox(VAppAccess app, VLayout layout, int x, int y, int width, int height) {
        super(app, layout, x, y, width, height);
    }

    //
    // Scroll data management
    //

    public void setScrollX(double scrollX) {
        scrollX = MathHelper.clamp(scrollX, -getMaxScrollX(), 0);
        this.scrollX = scrollX;

        for (VWidget<?> widget : getWidgets()) {
            widget.offsetX = (int) this.scrollX;
        }
    }

    public double getScrollX() {
        return scrollX;
    }

    public void setScrollY(double scrollY) {
        scrollY = MathHelper.clamp(scrollY, -getMaxScrollY(), 0);
        this.scrollY = scrollY;

        for (VWidget<?> widget : getWidgets()) {
            widget.offsetY = (int) this.scrollY;
        }
    }

    public double getScrollY() {
        return scrollY;
    }

    public int getMaxScrollX() {
        if (maxScrollX == null) return Math.max(layout.getEffectiveWidth() - getWidth(), 0);
        else return maxScrollX;
    }

    public void setMaxScrollX(@Nullable Integer max) {
        maxScrollX = max;
    }

    public int getMaxScrollY() {
        if (maxScrollY == null) return Math.max(layout.getEffectiveHeight() - getHeight(), 0);
        else return maxScrollY;

    }

    public void setMaxScrollY(@Nullable Integer max) {
        maxScrollY = max;
    }

    //
    // Practical scroll application
    //

    @Override
    public void addWidget(VWidget<?> widget) {
        super.addWidget(widget);

        widget.offsetX += this.offsetX;
        widget.offsetY -= this.offsetY;
    }

    @Override
    public void removeWidget(VWidget<?> widget) {
        super.removeWidget(widget);

        widget.offsetX -= this.offsetX;
        widget.offsetY += this.offsetY;
    }

    @Override
    protected void handleDelegatedEvent(String event, VEventContext ctx) {
        super.handleDelegatedEvent(event, ctx);

        if (ctx instanceof VWidgetEvent.MouseScroll e) handleScrollEvent(e);
    }

    @Override
    public void handleBuiltinEvent(String event, VEventContext ctx) {
        super.handleBuiltinEvent(event, ctx);

        if (ctx instanceof VWidgetEvent.MouseScroll e) handleScrollEvent(e);
    }

    private void handleScrollEvent(VWidgetEvent.MouseScroll e) {
        if (e.horizontal() != 0) setScrollX(getScrollX() + e.horizontal() * deltaHPerScroll);
        if (e.vertical() != 0)   setScrollY(getScrollY() + e.vertical()   * deltaYPerScroll);
    }

    //
    // Rendering
    //
    @Override
    protected void updateLayoutSize(int width, int height) {} // dont update layout size

    @Override
    public void renderChildren(VRenderContext ctx) {
        ctx.withClip(0, 0, getWidth(), getHeight(), () -> {
            super.renderChildren(ctx);
        });
    }

    @Override
    public void renderContent(VRenderContext ctx) {

    }
}
