package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class VTabWidget extends VWidget<VTabWidget> {
    private int itemSpacingLeft = 4;
    private int itemSpacingRight = 4;

    private final LinkedHashMap<String, List<VWidget<?>>> tabs = new LinkedHashMap<>();
    private @Nullable Integer activeTab = null;
    private @Nullable Integer hoveredTab = null;

    public VTabWidget(VeraApp app) {
        super(0, 0, 100, 16, app);
    }

    @Override
    public void render() {
        StyleState state = createStyleState();

        VFont font = getStyle("font", state);
        VColor defaultBackgroundColor = getStyle("background-color", state);
        VColor selectedBackgroundColor = getStyle("background-color-selected", state);

        int marginX = 0;
        int i = -1;

        int x = getX();
        int y = getY();

        for (String key : tabs.keySet()) {
            int textWidth = Vera.provider.getTextWidth(key, font);

            i++;
            marginX += itemSpacingLeft;

            Vera.renderer.drawRect(app,
                    x + marginX - itemSpacingLeft, y,
                    itemSpacingLeft + itemSpacingRight + textWidth,
                    getHitboxHeight(), 0,
                    activeTab != null && activeTab == i ? selectedBackgroundColor: defaultBackgroundColor
            );

            Vera.renderer.drawText(app, x + marginX, y + 2, 0, key, font);

            marginX += textWidth + itemSpacingRight;
        }
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        super.handleBuiltinEvent(event, args);

        switch (event) {
            case "mouse-move" -> getHoveredTabIndex((int) args[0]);

            case "hover-enter" -> getHoveredTabIndex(Vera.getMouseX());
            case "hover-leave" -> hoveredTab = null;

            case "left-click" -> {
                if (!isValidTabIndex(hoveredTab)) return;
                events.fire("vtabwidget-tab-left-click", hoveredTab);
                setActiveTab(hoveredTab);
            }
            case "left-click-release" -> {
                if (!isValidTabIndex(hoveredTab)) return;
                events.fire("vtabwidget-tab-left-click-release", hoveredTab);
            }

            case "middle-click" -> {
                if (!isValidTabIndex(hoveredTab)) return;
                events.fire("vtabwidget-tab-middle-click", hoveredTab);
            }
            case "middle-click-release" -> {
                if (!isValidTabIndex(hoveredTab)) return;
                events.fire("vtabwidget-tab-middle-click-release", hoveredTab);
            }

            case "right-click" -> {
                if (!isValidTabIndex(hoveredTab)) return;
                events.fire("vtabwidget-tab-right-click", hoveredTab);
            }
            case "right-click-release" -> {
                if (!isValidTabIndex(hoveredTab)) return;
                events.fire("vtabwidget-tab-right-click-release", hoveredTab);
            }
        }
    }

    public boolean isValidTabIndex(@Nullable Integer index) {
        return (!(index == null || index < 0 || index >= tabs.size()));
    }

    public @Nullable String getHoveredTab(int mouseX) {
        int index = getHoveredTabIndex(mouseX);
        return !isValidTabIndex(index) ? null : (String) List.of(tabs.keySet().toArray()).get(index);
    }

    public int getHoveredTabIndex(int mouseX) {
        int relativeX = mouseX - getHitboxX();
        int currentX = 0;
        int index = 0;

        for (String tabName : tabs.keySet()) {
            int textWidth = Vera.provider.getTextWidth(tabName, getStyle("font", createStyleState()));
            int totalTabWidth = itemSpacingLeft + textWidth + itemSpacingRight;

            if (relativeX >= currentX && relativeX < currentX + totalTabWidth) {
                if (hoveredTab != null && hoveredTab != index) {
                    events.fire("vtabwidget-tab-hover-change", hoveredTab);
                }

                hoveredTab = index;
                return index;
            }

            currentX += totalTabWidth;
            index++;
        }

        return -1;
    }

    public @Nullable Integer getTabIndex(String tab) {
        return !tabs.containsKey(tab) ? null : List.of(tabs.keySet().toArray()).indexOf(tab);
    }

    public void onTabHoverChange(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-hover-change", (args) -> runnable.accept((int) args[0]));
    }

    public void onTabLeftClick(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-left-click", (args) -> runnable.accept((int) args[0]));
    }

    public void onTabLeftClickRelease(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-left-click-release", (args) -> runnable.accept((int) args[0]));
    }

    public void onTabMiddleClick(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-middle-click", (args) -> runnable.accept((int) args[0]));
    }

    public void onTabMiddleClickRelease(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-middle-click-release", (args) -> runnable.accept((int) args[0]));
    }

    public void onTabRightClick(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-right-click", (args) -> runnable.accept((int) args[0]));
    }

    public void onTabRightClickRelease(Consumer<Integer> runnable) {
        events.register("vtabwidget-tab-right-click-release", (args) -> runnable.accept((int) args[0]));
    }

    public void addTab(String tab, VWidget<?>... widgets) {
        addTab(tab, Arrays.asList(widgets));
    }

    public void addTab(String tab, List<VWidget<?>> widgets) {
        if (!tabs.containsKey(tab)) {
            tabs.put(tab, new ArrayList<>());
        }

        addWidget(tab, widgets);
    }

    public void addWidget(String tab, VWidget<?>... widgets) {
        addWidget(tab, Arrays.asList(widgets));
    }

    public void addWidget(String tab, List<VWidget<?>> widgets) {
        if (tab == null || !tabs.containsKey(tab)) {
            throw new IllegalArgumentException("Failed to add " + widgets.size() + " widget(s) to tab '" + tab + "', because it doesn't exist. (App: " + app.getClass().getSimpleName() + ")");
        }

        Integer tabIndex = getTabIndex(tab);

        for (VWidget<?> widget : widgets) {
            widget.addVisibilityCondition(() -> isValidTabIndex(tabIndex) && Objects.equals(tabIndex, getActiveTab()));
            tabs.get(tab).add(widget);
        }
    }

    @Override
    public int getHitboxHeight() {
        return ((VFont) getStyle("font", createStyleState())).getSize() / 2 + 4;
    }

    @Override
    public int getHitboxWidth() {
        StyleState state = createStyleState();

        int currentX = 0;

        for (String tabName : tabs.keySet()) {
            int textWidth = Vera.provider.getTextWidth(tabName, getStyle("font", state));
            int totalTabWidth = itemSpacingLeft + textWidth + itemSpacingRight;

            currentX += totalTabWidth;
        }

        return currentX;
    }

    public @Nullable Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(@Nullable Integer activeTab) {
        if (activeTab == null || activeTab >= tabs.keySet().size()) {
            activeTab = null;
        }

        this.activeTab = activeTab;
    }

    public VFont.FontModifier modifyFont() {
        return app.styleSheet.modifyKeyAsFont(this, "font");
    }

    public VColor.ColorModifier modifyFontColor() {
        return app.styleSheet.modifyKeyAsFontColor(this, "font");
    }

    public VColor.ColorModifier modifyBackgroundColorSelected() {
        return app.styleSheet.modifyKeyAsColor(this, "background-color-selected");
    }

    public VColor.ColorModifier modifyBackgroundColor() {
        return app.styleSheet.modifyKeyAsColor(this, "background-color");
    }

    public int getItemSpacingLeft() {
        return itemSpacingLeft;
    }

    public void setItemSpacingLeft(int itemSpacingLeft) {
        this.itemSpacingLeft = itemSpacingLeft;
    }

    public int getItemSpacingRight() {
        return itemSpacingRight;
    }

    public void setItemSpacingRight(int itemSpacingRight) {
        this.itemSpacingRight = itemSpacingRight;
    }
}
