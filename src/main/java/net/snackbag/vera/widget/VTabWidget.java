package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class VTabWidget extends VWidget<VTabWidget> {
    private VFont font;
    private VColor selectedBackgroundColor;
    private VColor defaultBackgroundColor;
    private int itemSpacingLeft = 4;
    private int itemSpacingRight = 4;

    private final LinkedHashMap<String, List<VWidget<?>>> tabs = new LinkedHashMap<>();
    private Integer activeTab = null;

    public VTabWidget(VeraApp app, String... tabs) {
        super(0, 0, 100, 16, app);

        font = VFont.create();
        selectedBackgroundColor = VColor.white();
        defaultBackgroundColor = VColor.white().sub(40);

        setHoverCursor(VCursorShape.POINTING_HAND);
    }

    @Override
    public void render() {
        int marginX = 0;
        int i = -1;

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
            case "mouse-move" -> {
                System.out.println("tab: " + getHoveredTab((int) args[0]));
            }

            case "hover-enter" -> {

            }

            case "hover-leave" -> {

            }
        }
    }

    public @Nullable String getHoveredTab(int mouseX) {
        int index = getHoveredTabIndex(mouseX);
        return (index < 0 || index >= tabs.size()) ? null : (String) List.of(tabs.keySet().toArray()).get(index);
    }

    public int getHoveredTabIndex(int mouseX) {
        int relativeX = mouseX - getHitboxX();
        int currentX = 0;
        int index = 0;

        for (String tabName : tabs.keySet()) {
            int textWidth = Vera.provider.getTextWidth(tabName, font);
            int totalTabWidth = itemSpacingLeft + textWidth + itemSpacingRight;

            if (relativeX >= currentX && relativeX < currentX + totalTabWidth) {
                return index;
            }

            currentX += totalTabWidth;
            index++;
        }

        return -1;
    }

    public void addTab(String tab, VWidget<?>... widgets) {
        addTab(tab, Arrays.asList(widgets));
    }

    public void addTab(String tab, List<VWidget<?>> widgets) {
        if (tabs.containsKey(tab)) {
            addWidget(tab, widgets);
        }

        tabs.put(tab, widgets);
    }

    public void addWidget(String tab, VWidget<?>... widgets) {
        addWidget(tab, Arrays.asList(widgets));
    }

    public void addWidget(String tab, List<VWidget<?>> widgets) {
        if (tab == null) {
            throw new IllegalArgumentException("Failed to add " + widgets.size() + " widget(s) to tab '" + tab + ",' because it doesn't exist. (App: " + app.getClass().getSimpleName() + ")");
        }
    }

    @Override
    public int getHitboxHeight() {
        return font.getSize() / 2 + 4;
    }

    @Override
    public int getHitboxWidth() {
        int currentX = 0;

        for (String tabName : tabs.keySet()) {
            int textWidth = Vera.provider.getTextWidth(tabName, font);
            int totalTabWidth = itemSpacingLeft + textWidth + itemSpacingRight;

            currentX += totalTabWidth;
        }

        return currentX;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        if (activeTab >= tabs.keySet().size()) {
            System.out.println("n=ste to null cuz " + activeTab + " >= " + tabs.keySet().size());
            activeTab = null;
        }
        this.activeTab = activeTab;
    }

    public VFont getFont() {
        return font;
    }

    public void setFont(VFont font) {
        this.font = font;
    }

    public VFont.FontModifier modifyFont() {
        return new VFont.FontModifier(font, this::setFont);
    }

    public VColor.ColorModifier modifyFontColor() {
        return new VColor.ColorModifier(font.getColor(), (color) -> setFont(getFont().withColor(color)));
    }

    public VColor getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public void setSelectedBackgroundColor(VColor backgroundColor) {
        this.selectedBackgroundColor = backgroundColor;
    }

    public VColor.ColorModifier modifySelectedBackgroundColor() {
        return new VColor.ColorModifier(selectedBackgroundColor, this::setSelectedBackgroundColor);
    }

    public VColor getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public void setDefaultBackgroundColor(VColor defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    public VColor.ColorModifier modifyDefaultBackgroundColor() {
        return new VColor.ColorModifier(defaultBackgroundColor, this::setDefaultBackgroundColor);
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
