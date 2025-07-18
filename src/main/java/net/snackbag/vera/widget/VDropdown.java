package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.VItemSwitchEvent;
import net.snackbag.vera.modifier.VHasFont;
import net.snackbag.vera.style.StyleState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// TODO: Rewrite VDropdown from scratch
// 16/7/2025 jesus christ what a shitty thing. dont even bother making this work nice.
//           rewrite scheduled for once we have VCompound
public class VDropdown extends VWidget<VDropdown> implements VHasFont {
    private final List<Item> items;
    public VFont itemHoverFont;
    private VColor itemHoverColor;

    private int selectedItem = 0;
    private int itemSpacing = 0;
    private @Nullable Integer hoveredItem = null;

    public VDropdown(VeraApp app) {
        super(0, 0, 100, 16, app);

        items = new ArrayList<>();
        itemHoverFont = VFont.create();
        itemHoverColor = VColor.white().sub(30);
    }

    @Override
    public void render() {
        StyleState state = createStyleState();

        VColor backgroundColor = getStyle("background-color", state);
        VFont font = getStyle("font", state);

        int x = getX();
        int y = getY();

        Vera.renderer.drawRect(
                app, getHitboxX(), getHitboxY(), getHitboxWidth(), getHitboxHeight(),
                0, backgroundColor
        );

        if (isFocused()) {
            for (int i = 0; i < items.size(); i++) {
                boolean isHovered = hoveredItem != null && i == hoveredItem;
                Item item = items.get(i);

                int textY = y + (i * (itemSpacing + font.getSize() / 2) + itemSpacing / 2);
                int textX = (int) (item.icon == null ? x : x + font.getSize() * 0.7);

                if (isHovered) {
                    Vera.renderer.drawRect(
                            app,
                            getHitboxX(),
                            y + (i * (itemSpacing + font.getSize() / 2)),
                            getHitboxWidth(),
                            font.getSize() / 2 + itemSpacing,
                            0, itemHoverColor
                    );
                }

                if (item.icon != null) {
                    Vera.renderer.drawImage(
                            app, x, textY,
                            font.getSize() / 2,
                            font.getSize() / 2,
                            0, isHovered ? item.getHoverIcon() : item.getIcon()
                    );
                }

                Vera.renderer.drawText(app, textX, textY, 0, item.name, isHovered ? itemHoverFont : font);
            }
        } else {
            Vera.renderer.drawText(app, x, y, 0, getItems().get(selectedItem).name, font);
        }
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);

        if (focused) events.fire("vdropdown-selector-open");
        else events.fire("vdropdown-selector-close");
    }

    public VColor getItemHoverColor() {
        return itemHoverColor;
    }

    public void setItemHoverColor(VColor itemHoveredColor) {
        this.itemHoverColor = itemHoveredColor;
    }

    public VColor.ColorModifier modifyItemHoverColor() {
        return new VColor.ColorModifier(itemHoverColor, this::setItemHoverColor);
    }

    @Override
    public int getHitboxX() {
        V4Int padding = getStyle("padding", createStyleState());
        return getX() - padding.get3();
    }

    @Override
    public int getHitboxY() {
        V4Int padding = getStyle("padding", createStyleState());
        return getY() - padding.get1();
    }

    @Override
    public int getHitboxWidth() {
        V4Int padding = getStyle("padding", createStyleState());
        return width + padding.get3() + padding.get4();
    }

    @Override
    public int getHitboxHeight() {
        StyleState state = createStyleState();

        VFont font = getStyle("font", state);
        V4Int padding = getStyle("padding", state);

        return !isFocused() ?
                font.getSize() / 2 + padding.get1() + padding.get2() :
                items.size() * (font.getSize() / 2 + itemSpacing) + padding.get1() + padding.get2();
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        int x = getX();
        int y = getY();

        switch (event) {
            case "left-click" -> {
                if (isFocused()) {
                    Item target = getHoveredItem();
                    if (target != null && hoveredItem != null) {
                        target.safeRunLeftClickExecutor();
                        setSelectedItem(hoveredItem);
                    }

                    setFocused(false);
                    return;
                }
            }

            case "right-click" -> {
                if (isFocused()) {
                    Item target = getHoveredItem();
                    if (target != null && hoveredItem != null) {
                        target.safeRunRightClickExecutor();
                        setSelectedItem(hoveredItem);
                    }

                    setFocused(false);
                    return;
                }
            }

            case "middle-click" -> {
                if (isFocused()) {
                    Item target = getHoveredItem();
                    if (target != null && hoveredItem != null) {
                        target.safeRunMiddleClickExecutor();
                        setSelectedItem(hoveredItem);
                    }

                    setFocused(false);
                    return;
                }
            }

            case "mouse-move" -> {
                if (!isFocused()) hoveredItem = null;
                else {
                    // Get mouse position relative to the dropdown's top-left corner
                    int argX = (int) args[0];
                    int argY = (int) args[1];

                    int mouseX = argX - x;
                    int mouseY = argY - y;

                    Item item = getItemAt(mouseX, mouseY);
                    hoveredItem = (item != null) ? items.indexOf(item) : null;
                }
            }

            case "hover-leave" -> hoveredItem = null;
        }

        super.handleBuiltinEvent(event, args);
    }

    private int getItemIndexAt(int mouseY) {
        if (mouseY < 0) return -1;
        VFont font = getStyle("font", createStyleState());

        int index = mouseY / (itemSpacing + font.getSize() / 2);
        return items.size() < index ? -1 : index;
    }

    public void onItemSwitch(VItemSwitchEvent runnable) {
        events.register("vdropdown-item-switch", args -> runnable.run((int) args[0]));
    }

    public void onSelectorOpen(Runnable runnable) {
        events.register("vdropdown-selector-open", runnable);
    }

    public void onSelectorClose(Runnable runnable) {
        events.register("vdropdown-selector-close", runnable);
    }

    private @Nullable Item getItemAt(int mouseX, int mouseY) {
        int index = getItemIndexAt(mouseY);
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public @Nullable Item getHoveredItem() {
        return hoveredItem == null ? null : items.get(hoveredItem);
    }

    public @Nullable Integer getHoveredIndex() {
        return hoveredItem;
    }

    public int getItemSpacing() {
        return itemSpacing;
    }

    public void setItemSpacing(int itemSpacing) {
        this.itemSpacing = itemSpacing;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        events.fire("vdropdown-item-switch", selectedItem);
    }

    public void addItem(String name) {
        items.add(new Item(name, null, null, null, null, null));
    }

    public void addItem(String name, Identifier icon) {
        items.add(new Item(name, null, null, null, icon, null));
    }

    public void addItem(String name, Runnable leftClick) {
        items.add(new Item(name, leftClick, null, null, null, null));
    }

    public void addItem(String name, Runnable leftClick, Identifier icon) {
        items.add(new Item(name, leftClick, null, null, icon, null));
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public @Nullable Item getItem(int index) {
        return items.size() > index ? items.get(index) : null;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public VeraApp getApp() {
        return app;
    }

    public static class Item {
        private String name;
        private final @Nullable Runnable leftClick;
        private final @Nullable Runnable middleClick;
        private final @Nullable Runnable rightClick;
        private @Nullable Identifier icon;
        private @Nullable Identifier hoverIcon;

        public Item(String name,
                    @Nullable Runnable leftClick,
                    @Nullable Runnable middleClick,
                    @Nullable Runnable rightClick,
                    @Nullable Identifier icon,
                    @Nullable Identifier hoverIcon) {

            this.name = name;
            this.leftClick = leftClick;
            this.middleClick = middleClick;
            this.rightClick = rightClick;
            this.icon = icon;
            this.hoverIcon = hoverIcon;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public @Nullable Runnable getLeftClickExecutor() {
            return leftClick;
        }

        public @Nullable Runnable getMiddleClickExecutor() {
            return middleClick;
        }

        public @Nullable Runnable getRightClickExecutor() {
            return rightClick;
        }

        public @Nullable Identifier getIcon() {
            return icon;
        }

        /**
         * If no hover icon has been set it automatically returns the normal icon
         * @return the hover icon
         */
        public @Nullable Identifier getHoverIcon() {
            return hoverIcon == null ? icon : hoverIcon;
        }

        public void setIcon(@Nullable Identifier icon) {
            this.icon = icon;
        }

        public void setHoverIcon(@Nullable Identifier hoverIcon) {
            this.hoverIcon = hoverIcon;
        }

        public void safeRunLeftClickExecutor() {
            if (leftClick != null) leftClick.run();
        }

        public void safeRunMiddleClickExecutor() {
            if (middleClick != null) middleClick.run();
        }

        public void safeRunRightClickExecutor() {
            if (rightClick != null) rightClick.run();
        }
    }
}
