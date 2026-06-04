package net.snackbag.vera.widget;

import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VWidgetEvent;
import net.snackbag.vera.layout.VLayout;

public class VScrollBox extends VCompound<VScrollBox> {
    private double scrollX = 0;
    private double scrollY = 0;

    public VScrollBox(int x, int y, int width, int height, VLayout layout, VAppAccess app) {
        super(x, y, width, height, layout, app);
    }

    //
    // Scroll data management
    //

    public void setScrollX(double scrollX) {
        double old = this.scrollX;
        this.scrollX = scrollX;
        double diff = this.scrollX - old;

        for (VWidget<?> widget : getWidgets()) {
            widget.offsetX += (int) diff;
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
            widget.offsetY -= (int) diff;
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
        setScrollX(getScrollX() + e.horizontal());
        setScrollY(getScrollY() + e.vertical());
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
