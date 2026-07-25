package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.core.VAppAccess;
import org.joml.Vector2i;

public class VXLayout extends VLayout {
    public VXLayout(VAppAccess app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VXLayout(VAppAccess app, int x, int y) {
        this(app, x, y, -1, -1);
    }

    public VXLayout(VLayout parent, int width, int height) {
        this(parent.appAccess, 0, 0, width, height);
        this.alsoAddTo(parent);
    }

    public VXLayout(VLayout parent) {
        this(parent, -1, -1);
    }

    @Override
    protected Vector2i applyAlignment(Vector2i original) {
        return original;
    }

    @Override
    public void rebuild() {
        for (VElement elem : elements) {
            cache.put(elem, new Vector2i(getX() + elem.getRawX(), getY() + elem.getRawY()));
        }
    }

    public int calculateElementsHeight() {
        return elements.stream()
                .mapToInt((e) -> e.getRawY() + e.getEffectiveHeight())
                .max().orElse(1);
    }

    public int calculateElementsWidth() {
        return elements.stream()
                .mapToInt((e) -> e.getRawX() + e.getEffectiveWidth())
                .max().orElse(1);
    }
}
