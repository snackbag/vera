package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VDropdown extends VWidget<VDropdown> {
    private final List<Item> items;
    private VFont font;
    private VColor backgroundColor;
    private V4Int padding;

    private int selectedItem = 0;

    public VDropdown(VeraApp app) {
        super(0, 0, 100, 16, app);

        items = new ArrayList<>();
        font = VFont.create();
        backgroundColor = VColor.white();
        padding = new V4Int(5, 10);
    }

    @Override
    public void render() {
        if (isFocused()) {
            Vera.renderer.drawRect(
                    app,
                    x - padding.get3(),
                    y - padding.get1(),
                    width + padding.get3() + padding.get4(),
                    items.size() * (font.getSize() / 2) + padding.get1() + padding.get2(),
                    0, backgroundColor
            );

            for (int i = 0; i < items.size(); i++) {
                Vera.renderer.drawText(app, x, y + (i * (font.getSize() / 2)), 0, items.get(i).name, font);
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

    public V4Int getPadding() {
        return padding;
    }

    public void setPadding(V4Int padding) {
        this.padding = padding;
    }

    public void setPadding(int t, int b, int l, int r) {
        this.padding = new V4Int(t, b, l, r);
    }

    public void setPadding(int tb, int lr) {
        this.padding = new V4Int(tb, lr);

    }

    public void setPadding(int all) {
        this.padding = new V4Int(all);
    }

    @Override
    public void handleBuiltinEvent(String event) {
        if (event.equals("left-click")) {
            if (isFocused()) {
                setFocused(false);
                return;
            }
        }

        super.handleBuiltinEvent(event);
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
        items.add(new Item(name, null, null, null));
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public record Item(
            String name,
            @Nullable Runnable leftClick,
            @Nullable Runnable middleClick,
            @Nullable Runnable rightClick
    ) {}
}
