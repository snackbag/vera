package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.*;
import net.snackbag.vera.layout.VLayout;
import net.snackbag.vera.style.VEffectState;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.util.VGeometry;
import net.snackbag.vera.util.VMath;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Consumer;

public class VScrollBox extends VCompound<VScrollBox> {
    public int deltaYPerScroll = 10;
    public int deltaHPerScroll = 10;

    private double scrollX = 0;
    private double scrollY = 0;
    private @Nullable Integer maxScrollX = null;
    private @Nullable Integer maxScrollY = null;

    private final Bar vertBar;
    private final Bar horzBar;

    public VScrollBox(VAppAccess app, VLayout layout, int x, int y, int width, int height) {
        super(app, layout, x, y, width, height);

        this.vertBar = new Bar(this, 0, 0, false);
        this.horzBar = new Bar(this, 0, 0, true);

        addWidget(vertBar, false);
        addWidget(horzBar, false);
    }

    //
    // Scroll data management
    //

    public void setScrollX(double scrollX) {
        scrollX = VMath.clamp(scrollX, 0, getMaxScrollX());
        double before = this.scrollX;
        this.scrollX = scrollX;

        for (VWidget<?> widget : getWidgets()) {
            if (widget instanceof Bar) continue;
            widget.offsetX = (int) -this.scrollX;
        }

        events.fire(new VScrollBoxEvent.ScrolledX(before, this.scrollX));
    }

    public double getScrollX() {
        return scrollX;
    }

    public int getMaxScrollX() {
        if (maxScrollX == null) return Math.max(layout.getEffectiveWidth() - getWidth(), 0);
        else return maxScrollX;
    }

    public void setMaxScrollX(@Nullable Integer max) {
        maxScrollX = max;
    }

    public float getScrollProgressX() {
        return (float) getScrollX() / getMaxScrollX();
    }

    public boolean shouldRenderXBar() {
        return getMaxScrollX() > 0;
    }

    public void setScrollY(double scrollY) {
        scrollY = VMath.clamp(scrollY, 0, getMaxScrollY());
        double before = this.scrollY;
        this.scrollY = scrollY;

        for (VWidget<?> widget : getWidgets()) {
            if (widget instanceof Bar) continue;
            widget.offsetY = (int) -this.scrollY;
        }

        events.fire(new VScrollBoxEvent.ScrolledY(before, this.scrollY));
    }

    public double getScrollY() {
        return scrollY;
    }

    public int getMaxScrollY() {
        if (maxScrollY == null) return Math.max(layout.getEffectiveHeight() - getHeight(), 0);
        else return maxScrollY;
    }

    public void setMaxScrollY(@Nullable Integer max) {
        maxScrollY = max;
    }

    public float getScrollProgressY() {
        return (float) getScrollY() / getMaxScrollY();
    }

    public boolean shouldRenderYBar() {
        return getMaxScrollY() > 0;
    }

    //
    // Practical scroll application
    //

    @Override
    protected void addWidget(VWidget<?> widget, boolean addToLayout) {
        super.addWidget(widget, addToLayout);

        if (!(widget instanceof Bar)) {
            widget.offsetX += this.offsetX;
            widget.offsetY -= this.offsetY;
        }
    }

    @Override
    public void removeWidget(VWidget<?> widget) {
        super.removeWidget(widget);

        if (!(widget instanceof Bar)) {
            widget.offsetX -= this.offsetX;
            widget.offsetY += this.offsetY;
        }
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
        if (e.horizontal() != 0) setScrollX(getScrollX() - e.horizontal() * deltaHPerScroll);
        if (e.vertical() != 0)   setScrollY(getScrollY() - e.vertical()   * deltaYPerScroll);
    }

    @Override
    public boolean isDelegatedPointOver(int px, int py, VWidget<?> widget) {
        return VGeometry.isInBox(px, py, getX(), getY(), getWidth(), getHeight());
    }

    //
    // Events
    //
    public void onScrolledX(Consumer<VScrollBoxEvent.ScrolledX> ctx) {
        events.register(VEvents.ScrollBox.SCROLLED_X, ctx);
    }

    public void onScrolledY(Consumer<VScrollBoxEvent.ScrolledY> ctx) {
        events.register(VEvents.ScrollBox.SCROLLED_Y, ctx);
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
        VStyleState state = createStyleState();

        Vera.renderer.drawFill(
                ctx,
                0, 0,
                getEffectiveWidth(), getEffectiveHeight(),
                getStyle("background", state)
        );
    }

    public static class Bar extends VWidget<Bar> {
        private final VScrollBox parent;
        private final boolean horizontal;

        private enum Region {NONE, DECREMENT, INCREMENT, THUMB, TRACK_BEFORE, TRACK_AFTER}

        private static final EnumSet<Region> TRACK_REGIONS = EnumSet.of(Region.TRACK_BEFORE, Region.TRACK_AFTER);

        private record BarGeometry(int n, int length, int trackStart, int trackLength, int thumbSize, int thumbOffset) {
        }

        // Position on the bar's axis
        private int lastRelPos = -1;
        private Region hoveredRegion = Region.NONE;

        private boolean draggingThumb = false;
        private int dragStartThumbOffset = 0;
        private int dragStartLocalPos = 0;

        private Bar(VScrollBox parent, int x, int y, boolean horizontal) {
            super(parent, x, y, 0, 0);

            this.parent = parent;
            this.horizontal = horizontal;

            addVisibilityCondition(() -> horizontal ? parent.shouldRenderXBar() : parent.shouldRenderYBar());
        }

        @Override
        public int getWidth() {
            if (horizontal) return parent.getWidth();
            else return getStyle("scroll-width", createStyleState());
        }

        @Override
        public int getHeight() {
            if (horizontal) return getStyle("scroll-width", createStyleState());
            else return parent.getHeight();
        }

        @Override
        public int getX() {
            int x = super.getX() + parent.getX();
            if (!horizontal) x += parent.getWidth() - getWidth();
            return x;
        }

        @Override
        public int getY() {
            int y = super.getY() + parent.getY();
            if (horizontal) y += parent.getHeight() - getHeight();
            return y;
        }

        //
        // Geometry / hit testing
        //

        private BarGeometry createGeometry() {
            int n = Math.min(getWidth(), getHeight());
            int length = horizontal ? getWidth() : getHeight();

            int trackStart = n;
            int trackLength = Math.max(length - 2 * n, 0);

            int visibleSize = horizontal ? parent.getWidth() : parent.getHeight();
            int maxScroll = horizontal ? parent.getMaxScrollX() : parent.getMaxScrollY();
            int contentSize = visibleSize + maxScroll;

            float visibleRatio = contentSize > 0 ? (float) visibleSize / contentSize : 1f;
            int thumbSize = Math.min(Math.max((int) (trackLength * visibleRatio), n), trackLength);

            float progress = horizontal ? parent.getScrollProgressX() : parent.getScrollProgressY();
            if (Float.isNaN(progress)) progress = 0f;

            int thumbOffset = trackStart + Math.round((trackLength - thumbSize) * progress);

            return new BarGeometry(n, length, trackStart, trackLength, thumbSize, thumbOffset);
        }

        private Region regionAt(int pos) {
            BarGeometry g = createGeometry();

            if (pos < g.n()) return Region.DECREMENT;
            if (pos >= g.length() - g.n()) return Region.INCREMENT;
            if (pos >= g.thumbOffset() && pos < g.thumbOffset() + g.thumbSize()) return Region.THUMB;
            if (pos < g.thumbOffset()) return Region.TRACK_BEFORE;
            return Region.TRACK_AFTER;
        }

        //
        // Click handling
        //

        @Override
        public void handleBuiltinEvent(String event, VEventContext ctx) {
            super.handleBuiltinEvent(event, ctx);

            if (!visibilityConditionsPassed()) return;

            switch (event) {
                case VEvents.Widget.MOUSE_MOVE -> {
                    if (ctx instanceof VWidgetEvent.MouseMove e) {
                        int localX = e.x() - getX();
                        int localY = e.y() - getY();
                        lastRelPos = horizontal ? localX : localY;
                        hoveredRegion = regionAt(lastRelPos);
                    }
                }

                case VEvents.Widget.MOUSE_DRAG -> {
                    if (ctx instanceof VWidgetEvent.MouseDrag e) handleDrag(e);
                }

                case VEvents.Widget.HOVER_LEAVE -> {
                    hoveredRegion = Region.NONE;
                    lastRelPos = -1;
                }

                case VEvents.Widget.LEFT_CLICK -> {
                    Region region = lastRelPos >= 0 ? regionAt(lastRelPos) : Region.NONE;
                    handleClick(region);
                }

                case VEvents.Widget.LEFT_CLICK_RELEASE -> draggingThumb = false;
            }
        }

        private void handleClick(Region region) {
            switch (region) {
                case DECREMENT -> {
                    if (horizontal) parent.setScrollX(parent.getScrollX() - parent.deltaHPerScroll);
                    else parent.setScrollY(parent.getScrollY() - parent.deltaYPerScroll);
                }

                case INCREMENT -> {
                    if (horizontal) parent.setScrollX(parent.getScrollX() + parent.deltaHPerScroll);
                    else parent.setScrollY(parent.getScrollY() + parent.deltaYPerScroll);
                }

                case TRACK_BEFORE, TRACK_AFTER -> moveThumbTo(lastRelPos);
            }
        }

        private void moveThumbTo(int pos) {
            BarGeometry g = createGeometry();
            int maxOffset = g.trackStart() + Math.max(g.trackLength() - g.thumbSize(), 0);

            // center the thumb under the click point, clamped to the track
            int targetOffset = VMath.clamp(pos - g.thumbSize() / 2, g.trackStart(), maxOffset);

            int range = g.trackLength() - g.thumbSize();
            float progress = range > 0 ? (float) (targetOffset - g.trackStart()) / range : 0f;

            int maxScroll = horizontal ? parent.getMaxScrollX() : parent.getMaxScrollY();
            double newScroll = progress * maxScroll;

            if (horizontal) parent.setScrollX(newScroll);
            else parent.setScrollY(newScroll);
        }

        private void handleDrag(VWidgetEvent.MouseDrag e) {
            if (e.button() != VMouseButton.LEFT) return;

            int startLocal = horizontal ? e.startX() - getX() : e.startY() - getY();
            int currentLocal = horizontal ? e.currentX() - getX() : e.currentY() - getY();

            if (!draggingThumb) {
                BarGeometry startGeometry = createGeometry();
                draggingThumb = true;
                dragStartThumbOffset = startGeometry.thumbOffset();
                dragStartLocalPos = startLocal;
            }

            BarGeometry g = createGeometry();
            int delta = currentLocal - dragStartLocalPos;
            int maxOffset = g.trackStart() + Math.max(g.trackLength() - g.thumbSize(), 0);

            int newThumbOffset = VMath.clamp(dragStartThumbOffset + delta, g.trackStart(), maxOffset);

            int range = g.trackLength() - g.thumbSize();
            float progress = range > 0 ? (float) (newThumbOffset - g.trackStart()) / range : 0f;

            int maxScroll = horizontal ? parent.getMaxScrollX() : parent.getMaxScrollY();
            double newScroll = progress * maxScroll;

            if (horizontal) parent.setScrollX(newScroll);
            else parent.setScrollY(newScroll);
        }

        //
        // Rendering
        //

        private VStyleState regionStyleState(Region region) {
            boolean isTrackMatch = TRACK_REGIONS.contains(region);

            boolean clickedHere = isLeftClickDown() && lastRelPos >= 0
                    && (isTrackMatch ? TRACK_REGIONS.contains(regionAt(lastRelPos)) : regionAt(lastRelPos) == region);
            boolean hoveredHere = isHovered() && (isTrackMatch ? TRACK_REGIONS.contains(hoveredRegion) : hoveredRegion == region);

            if (clickedHere) return VEffectState.LEFT_CLICKED.asStyleState();
            if (hoveredHere) return VEffectState.HOVERED.asStyleState();
            return VEffectState.DEFAULT.asStyleState();
        }

        @Override
        public void renderContent(VRenderContext ctx) {
            if (!visibilityConditionsPassed()) return;

            BarGeometry g = createGeometry();

            VFill decrement = getStyle("scroll-decrement", regionStyleState(Region.DECREMENT));
            VFill increment = getStyle("scroll-increment", regionStyleState(Region.INCREMENT));
            VFill thumb = getStyle("scroll-thumb", regionStyleState(Region.THUMB));
            VFill track = getStyle("scroll-track", regionStyleState(Region.TRACK_BEFORE));

            Vera.renderer.drawFill(ctx, 0, 0, getWidth(), getHeight(), track);

            if (horizontal) {
                Vera.renderer.drawFill(ctx, 0, 0, g.n(), g.n(), decrement);
                Vera.renderer.drawFill(ctx, g.length() - g.n(), 0, g.n(), g.n(), increment);
                Vera.renderer.drawFill(ctx, g.thumbOffset(), 0, g.thumbSize(), g.n(), thumb);
            } else {
                Vera.renderer.drawFill(ctx, 0, 0, g.n(), g.n(), decrement);
                Vera.renderer.drawFill(ctx, 0, g.length() - g.n(), g.n(), g.n(), increment);
                Vera.renderer.drawFill(ctx, 0, g.thumbOffset(), g.n(), g.thumbSize(), thumb);
            }
        }
    }
}
