package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.core.VeraApp;
import org.joml.Vector2i;

public class VHLayout extends VLayout {
    public VHLayout(VeraApp app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VHLayout(VeraApp app, int x, int y) {
        this(app, x, y, -1, -1);
    }

    public VHLayout(VLayout parent, int width, int height) {
        this(parent.app, 0, 0, width, height);
        this.alsoAddTo(parent);
    }

    public VHLayout(VLayout parent) {
        this(parent, -1, -1);
    }

    @Override
    public void rebuild() {
        int x = getX();

        for (VElement elem : elements) {
            cache.put(elem, new Vector2i(x, getY()));

            x += elem.getWidth();
        }
    }
}
