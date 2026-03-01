package net.snackbag.vera.widget;

import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VAppAccess;
import net.snackbag.vera.core.VFill;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VRenderContext;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VItemAddedEvent;
import net.snackbag.vera.event.VItemRemovedEvent;
import net.snackbag.vera.layout.VVLayout;
import net.snackbag.vera.style.VStyleState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class VComboBox extends VCompound<VComboBox> {
    private int selectedItemIndex = 0;

    public VComboBox(VAppAccess app) {
        this(0, 0, 100, 16, app);
    }

    public VComboBox(int x, int y, int width, int height, VAppAccess app) {
        super(x, y, width, height, new VVLayout(app.get(), x, y, width, height), app);

        layout.addVisibilityCondition(this::isFocused);
    }

    @Override
    public void renderContent(VRenderContext ctx) {
        VStyleState state = createStyleState();

        V4Int padding = getStyle("padding", state);
        VFill background = getStyle("background", state);
        VFont font = getStyle("font", state);
        VFill arrow = isFocused() ? getStyle("arrow-focused") : getStyle("arrow", state);

        int width = getEffectiveWidth();
        int height = getEffectiveHeight();

        Item item = getSelectedItem();
        int textHeight = 8;
        String text = "";

        if (item != null) {
            textHeight = Vera.provider.getTextHeight(
                    item.getText(),
                    item.getStyle("ci-font", item.createStyleState())
            );
            text = item.getText();
        }

        Vera.renderer.drawFill(ctx, 0, 0, width, height, background);
        Vera.renderer.drawFill(ctx, this.width - textHeight, height / 2 - textHeight / 2, textHeight, textHeight, arrow);
        Vera.renderer.drawText(ctx, padding.get3(), height / 2 - textHeight / 2, text, font);
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        layout.move(getX(), getY() + getEffectiveHeight());
    }

    @Override
    public int getEffectiveWidth() {
        V4Int padding = getStyle("padding", createStyleState());
        return getWidth() + padding.get3() + padding.get4();
    }

    @Override
    public int getEffectiveHeight() {
        V4Int padding = getStyle("padding", createStyleState());
        return getHeight() + padding.get1() + padding.get2();
    }

    @Override
    protected void handleDelegatedEvent(String event, Object[] args) {
        super.handleDelegatedEvent(event, args);
        if (event.equals(VEvents.Widget.LEFT_CLICK)) {
            VWidget<?> potentialItem = getHoveredWidget();
            if (potentialItem instanceof Item item) setSelectedItemIndex(getIndexOfItem(item));
        }
    }

    public int getIndexOfItem(Item item) {
        return getWidgets().indexOf(item);
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int item) {
        this.selectedItemIndex = Math.min(item, getWidgets().size());
        events.fire(VEvents.ComboBox.SELECTION_CHANGED);
    }

    public @Nullable Item getItemAtIndex(int index) {
        if (getWidgets().isEmpty()) return null;
        if (getWidgets().size() <= index || index < 0) return null;
        return (Item) getWidgets().get(index);
    }

    public @Nullable Item getSelectedItem() {
        return getItemAtIndex(selectedItemIndex);
    }

    public Item addItem(Consumer<Item> item) {
        Item i = new Item(this);
        item.accept(i);

        addWidget(i);
        events.fire(VEvents.ComboBox.ITEM_ADDED, item);

        return i;
    }

    public Item addItem(String name, Consumer<Item> item) {
        return addItem(i -> {
            i.setText(name);
            item.accept(i);
        });
    }

    public Item addItem(String name) {
        return addItem(item -> item.setText(name));
    }

    public void removeItem(int index) {
        if (getWidgets().size() <= index) {
            MinecraftVera.LOGGER.error("Couldn't remove item of index %s from VComboBox, because there are only %s items"
                    .formatted(index, getWidgets().size()));
            return;
        }

        events.fire(VEvents.ComboBox.ITEM_REMOVED, index);
        removeWidget(getWidgets().get(index));
    }

    public void clearItems() {
        for (VWidget<?> widget : getWidgets()) {
            removeWidget(widget);
        }
    }

    public void onSelectionChanged(Runnable runnable) {
        events.register(VEvents.ComboBox.SELECTION_CHANGED, runnable);
    }

    public void onItemAdded(VItemAddedEvent runnable) {
        events.register(VEvents.ComboBox.ITEM_ADDED, args -> runnable.run((Item) args[0]));
    }

    public void onItemRemoved(VItemRemovedEvent runnable) {
        events.register(VEvents.ComboBox.ITEM_REMOVED, args -> runnable.run((int) args[0]));
    }

    public static class Item extends VWidget<Item> {
        private final VComboBox parent;
        private String text;

        private Item(VComboBox parent) {
            super(0, 0, -1, -1, parent);
            this.parent = parent;
            this.text = "";
        }

        @Override
        public int getHeight() {
            return getStyle("ci-height", createStyleState());
        }

        @Override
        public int getWidth() {
            return parent.getEffectiveWidth();
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            events.fire(VEvents.ComboBox.ITEM_TEXT_CHANGED);
        }

        @Override
        public void renderContent(VRenderContext ctx) {
            VStyleState state = createStyleState();

            V4Int padding = parent.getStyle("padding", parent.createStyleState());
            VFill background = getStyle("ci-background", state);
            VFont font = getStyle("ci-font", state);
            int height = getStyle("ci-height", state);
            int width = getWidth();

            String text = getText();
            int textHeight = Vera.provider.getTextHeight(text, font);

            @Nullable VFill icon = getStyle("ci-icon", state);
            boolean reserveIconSpace = getStyle("ci-reserve-icon-space", state);
            int iconWidth = reserveIconSpace ? textHeight : -2;

            Vera.renderer.drawFill(ctx, 0, 0, width, height, background);

            if (icon != null) {
                Vera.renderer.drawFill(ctx, padding.get3(), height / 2 - textHeight / 2, iconWidth, iconWidth, icon);
            }

            Vera.renderer.drawText(ctx, padding.get3() + iconWidth + 2, height / 2 - textHeight / 2, getText(), font);
        }
    }
}
