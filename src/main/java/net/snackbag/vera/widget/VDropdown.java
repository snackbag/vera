package net.snackbag.vera.widget;

import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.V4Int;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VDropdown extends VWidget {
    private final List<Item> items;
    private VFont font;
    private VColor backgroundColor;
    private V4Int padding;

    public VDropdown(VeraApp app) {
        super(0, 0, 100, 16, app);

        items = new ArrayList<>();
        font = VFont.create();
        backgroundColor = VColor.white();
        padding = new V4Int(5, 10);
    }

    @Override
    public void render() {
        Vera.renderer.drawRect(
                app,
                x - padding.get3(),
                y - padding.get1(),
                width + padding.get3() + padding.get4(),
                items.size() * (font.getSize() / 2) + padding.get1() + padding.get2(),
                0, backgroundColor
        );

        for (int i = 0; i < items.size(); i++) {
            Vera.renderer.drawText(app, x, y + (i * (  font.getSize() / 2)), 0, items.get(i).name, font);
        }
    }

    @Override
    public int getRealX() {
        return x - padding.get3();
    }

    @Override
    public int getRealY() {
        return y - padding.get1();
    }

    @Override
    public int getFullWidth() {
        return width + padding.get3() + padding.get4();
    }

    @Override
    public int getFullHeight() {
        return items.size() * (font.getSize() / 2) + padding.get1() + padding.get2();
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

    public VFont getFont() {
        return font;
    }

    public void setFont(VFont font) {
        this.font = font;
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(VColor backgroundColor) {
        this.backgroundColor = backgroundColor;
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
