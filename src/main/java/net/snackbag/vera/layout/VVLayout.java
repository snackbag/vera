package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.core.VAppAccess;
import org.joml.Vector2i;

public class VVLayout extends VLayout {
    public VVLayout(VAppAccess app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VVLayout(VAppAccess app, int x, int y) {
        this(app, x, y, -1, -1);
    }

    public VVLayout(VLayout parent, int width, int height) {
        this(parent.appAccess, 0, 0, width, height);
        this.alsoAddTo(parent);
    }

    public VVLayout(VLayout parent) {
        this(parent, -1, -1);
    }

    @Override
    public void rebuild() {
        int y = getY();

        for (VElement elem : elements) {
            cache.put(elem, new Vector2i(getX(), y));

            y += elem.getEffectiveHeight();
        }
    }

    @Override
    protected Vector2i applyAlignment(Vector2i original) {
        return switch (alignment) {
            case START -> original;
            case CENTER -> new Vector2i(original.x, getEffectiveHeight() / 2 - calculateElementsHeight() / 2 + original.y);
            case END -> new Vector2i(original.x, getEffectiveHeight() - calculateElementsHeight() + original.y);
        };
    }
}
