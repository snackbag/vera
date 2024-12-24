package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.modifier.VPaddingWidget;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VDropdown extends VWidget<VDropdown> implements VPaddingWidget {
    private final List<Item> items;
    private VFont font;
    private VColor backgroundColor;
    private V4Int padding;

    private int selectedItem = 0;
    private int itemSpacing = 0;
    private @Nullable Integer hoveredItem = null;

    public VDropdown(VeraApp app) {
        super(0, 0, 100, 16, app);

        items = new ArrayList<>();
        font = VFont.create();
        backgroundColor = VColor.white();
        padding = new V4Int(5, 10);
        setHoverCursor(VCursorShape.POINTING_HAND);
    }

    @Override
    public void render() {
        if (isFocused()) {
            Vera.renderer.drawRect(
                    app,
                    x - padding.get3(),
                    y - padding.get1(),
                    width + padding.get3() + padding.get4(),
                    items.size() * (font.getSize() / 2 + itemSpacing) + padding.get1() + padding.get2(),
                    0, backgroundColor
            );

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                int itemY = y + (i * (font.getSize() / 2 + itemSpacing));
                if (item.icon != null) {
                    Vera.renderer.drawImage(
                            app,
                            x, itemY,
                            font.getSize() / 2, font.getSize() / 2,
                            0, item.icon
                    );

                    Vera.renderer.drawText(
                            app, x + (int) (font.getSize() * 0.70), itemY,
                            0, item.name, font
                    );
                } else {
                    Vera.renderer.drawText(app, x, itemY, 0, item.name, font);
                }
            }
        } else {
            Vera.renderer.drawRect(
                    app,
                    x - padding.get3(),
                    y - padding.get1(),
                    width + padding.get3() + padding.get4(),
                    font.getSize() / 2 + padding.get1() + padding.get2(),
                    0, backgroundColor
            );

            if (!items.isEmpty()) {
                Vera.renderer.drawText(app, x, y, 0, items.get(0).name, font);
            }
        }
    }

    @Override
    public int getHitboxX() {
        return x - padding.get3();
    }

    @Override
    public int getHitboxY() {
        return y - padding.get1();
    }

    @Override
    public int getHitboxWidth() {
        return width + padding.get3() + padding.get4();
    }

    @Override
    public int getHitboxHeight() {
        return font.getSize() / 2 + padding.get1() + padding.get2();
    }

    @Override
    public V4Int getPadding() {
        return padding;
    }

    @Override
    public void setPadding(V4Int padding) {
        this.padding = padding;
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        if (event.equals("left-click")) {
            if (isFocused()) {
                setFocused(false);
                return;
            }
        }

        super.handleBuiltinEvent(event, args);
    }

    private @Nullable Item getItemAt(int mouseX, int mouseY) {

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
        return new VColor.ColorModifier(font.getColor(), (color) -> setFont(font.withColor(color)));
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(VColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public VColor.ColorModifier modifyBackgroundColor() {
        return new VColor.ColorModifier(backgroundColor, this::setBackgroundColor);
    }

    public void addItem(String name) {
        items.add(new Item(name, null, null, null, null));
    }

    public void addItem(String name, Identifier icon) {
        items.add(new Item(name, null, null, null, icon));
    }

    public void addItem(String name, Runnable leftClick) {
        items.add(new Item(name, leftClick, null, null, null));
    }

    public void addItem(String name, Runnable leftClick, Identifier icon) {
        items.add(new Item(name, leftClick, null, null, icon));
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

    public static class Item {
        private String name;
        private final @Nullable Runnable leftClick;
        private final @Nullable Runnable middleClick;
        private final @Nullable Runnable rightClick;
        private @Nullable Identifier icon;

        public Item(String name,
                    @Nullable Runnable leftClick,
                    @Nullable Runnable middleClick,
                    @Nullable Runnable rightClick,
                    @Nullable Identifier icon) {

            this.name = name;
            this.leftClick = leftClick;
            this.middleClick = middleClick;
            this.rightClick = rightClick;
            this.icon = icon;
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

        public void setIcon(@Nullable Identifier icon) {
            this.icon = icon;
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
