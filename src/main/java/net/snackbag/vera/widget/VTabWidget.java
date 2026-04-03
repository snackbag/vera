package net.snackbag.vera.widget;

import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VFill;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VTabWidgetEvent;
import net.snackbag.vera.layout.VHLayout;
import net.snackbag.vera.modifier.VHasFont;
import net.snackbag.vera.style.VStyleState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class VTabWidget extends VCompound<VTabWidget> {
    private int cachedTotalTabWidthSum;
    private int cachedTabHeightMax;

    private int activeTab = 0;

    public VTabWidget(VAppAccess app) {
        this(0, 0, 0, 0, app);
    }

    public VTabWidget(int x, int y, int width, int height, VAppAccess app) {
        super(x, y, width, height, new VHLayout(app, x, y, width, height), app);
    }

    public int getActiveTabIndex() {
        return activeTab;
    }

    public @Nullable Tab getActiveTab() {
        return getTab(activeTab);
    }

    public void setActiveTab(int index) {
        Tab tab = getTab(index);
        if (tab == null) {
            MinecraftVera.LOGGER.warn("Couldn't set active tab to tab " + index + ", because there is no tab with that index. Size: " + getTabs().size());
            return;
        }

        activeTab = index;
    }

    public void setActiveTab(Tab tab) {
        setActiveTab(getTabIndex(tab.name));
    }

    public @Nullable Tab getTab(String name) {
        for (Tab tab : getTabs()) {
            if (tab.getName().equals(name)) return tab;
        }

        return null;
    }

    public @Nullable Tab getTab(int index) {
        return !isValidIndex(index) ? null : (Tab) getWidgets().get(index);
    }

    public int getTabIndex(String name) {
        Tab tab = getTab(name);
        if (tab == null) throw new NullPointerException("There is no tab with name " + name);
        return getTabs().indexOf(tab);
    }

    public void addTab(String name) {
        addWidget(new Tab(name, this));
        events.fire(new VTabWidgetEvent.TabAdded(name));
    }

    public void addTab(String name, VWidget<?>... widgets) {
        addTab(name);
        addWidgets(name, widgets);
    }

    public void removeTab(String name) {
        removeTab(getTabIndex(name));
    }

    public void removeTab(int index) {
        if (!isValidIndex(index)) {
            throw new ArrayIndexOutOfBoundsException("There is no tab with index " + index + "; size: " + getTabs().size());
        }

        removeWidget(getTab(index));
    }

    protected boolean isValidIndex(int index) {
        return !(index < 0 || index >= getTabs().size());
    }

    public void addWidgets(String tabName, VWidget<?>... widgets) {
        Tab tab = getTab(tabName);
        if (tab == null) throw new NullPointerException("Tab of name " + tabName + " does not exist");

        tab.addWidgets(widgets);
    }

    @Override
    public void addWidget(VWidget<?> widget) {
        if (!(widget instanceof Tab)) {
            throw new IllegalArgumentException("Don't use addWidget to add to a VTabWidget, use addWidgets instead!");
        }

        super.addWidget(widget);

        // update caches
        cachedTotalTabWidthSum = getTabs().stream()
                .mapToInt(Tab::getEffectiveWidth)
                .sum();
        cachedTabHeightMax = getTabs().stream()
                .mapToInt(Tab::getEffectiveHeight)
                .max()
                .orElse(0);
    }

    @Override
    public int getEffectiveWidth() {
        return super.getEffectiveWidth() + cachedTotalTabWidthSum;
    }

    @Override
    public int getEffectiveHeight() {
        return super.getEffectiveHeight() + cachedTabHeightMax;
    }

    @Override
    public void renderContent(VRenderContext ctx) {}

    public List<Tab> getTabs() {
        return Collections.unmodifiableList((List) getWidgets());
    }

    public static class Tab extends VWidget<Tab> implements VHasFont {
        private String name;
        private final VTabWidget parent;

        public Tab(String name, VTabWidget parent) {
            super(0, 0, 0, 0, parent);
            this.name = name;
            this.parent = parent;

            adjustSize();
        }

        private void adjustSize() {
            VFont font = getStyle("font", createStyleState());

            setSize(
                    Vera.provider.getTextWidth(name, font),
                    Vera.provider.getTextHeight(name, font)
            );
        }

        public void addWidgets(VWidget<?>... widgets) {
            for (VWidget<?> widget : widgets) {
                widget.addVisibilityCondition(() -> parent.getActiveTab() == this);
                events.fire(new VTabWidgetEvent.WidgetAdded(widget));
            }
        }

        public void setName(String name) {
            this.name = name;
            adjustSize();
            events.fire(new VTabWidgetEvent.TabNameChanged(name));
        }

        public String getName() {
            return name;
        }

        public void onTabNameChange(Consumer<VTabWidgetEvent.TabNameChanged> ctx) {
            events.register(VEvents.TabWidget.TAB_NAME_CHANGED, ctx);
        }

        @Override
        public int getEffectiveHeight() {
            V4Int padding = getStyle("padding", createStyleState());
            return super.getEffectiveHeight() + padding.get1() + padding.get2();
        }

        @Override
        public int getEffectiveWidth() {
            V4Int padding = getStyle("padding", createStyleState());
            return super.getEffectiveWidth() + padding.get3() + padding.get4();
        }

        @Override
        public void handleBuiltinEvent(String event, VEventContext ctx) {
            super.handleBuiltinEvent(event, ctx);

            if (event.equals(VEvents.Widget.LEFT_CLICK)) {
                parent.setActiveTab(this);
            }
        }

        @Override
        public void renderContent(VRenderContext ctx) {
            String suffix = parent.getActiveTab() == this ? "-selected" : "";

            VStyleState state = createStyleState();
            VFont font = getStyle("font" + suffix, state);
            VFill background = getStyle("background" + suffix, state);
            V4Int padding = getStyle("padding" + suffix, state);

            Vera.renderer.drawFill(ctx, 0, 0, getEffectiveWidth(), getEffectiveHeight(), background);
            Vera.renderer.drawText(ctx, padding.get3(), padding.get1(), getName(), font);
        }
    }
}
