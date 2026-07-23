package net.snackbag.vera.widget;

import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VWidgetEvent;
import net.snackbag.vera.layout.VLayout;

public class VScrollBox extends VCompound<VScrollBox> {
    public int deltaYPerScroll = 10;
    public int deltaHPerScroll = 10;

    private double scrollX = 0;
    private double scrollY = 0;

    public VScrollBox(VAppAccess app, VLayout layout, int x, int y, int width, int height) {
        super(app, layout, x, y, width, height);
    }

    //
    // Scroll data management
    //

    public void setScrollX(double scrollX) {
        double old = this.scrollX;
        this.scrollX = scrollX;
        double diff = this.scrollX - old;

        for (VWidget<?> widget : getWidgets()) {
            widget.offsetX += diff;
        }
    }

    public double getScrollX() {
        return scrollX;
    }

    public void setScrollY(double scrollY) {
        double old = this.scrollY;
        this.scrollY = scrollY;
        double diff = this.scrollY - old;

        for (VWidget<?> widget : getWidgets()) {
            widget.offsetY -= diff;
        }
    }

    public double getScrollY() {
        return scrollY;
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
    public void renderChildren(VRenderContext ctx) {
        ctx.withClip(0, 0, getWidth(), getHeight(), () -> {
            super.renderChildren(ctx);
        });
    }

    @Override
    public void renderContent(VRenderContext ctx) {

    }
}
