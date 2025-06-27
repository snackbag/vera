package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.core.VeraApp;
import org.joml.Vector2i;

public class VVLayout extends VLayout {
    public VVLayout(VeraApp app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VVLayout(VeraApp app, int x, int y) {
        this(app, x, y, -1, -1);
    }

    @Override
    public void rebuild() {
        int y = 0;

        for (VElement elem : elements) {
            cache.put(elem, new Vector2i(x, y));

            y += elem.getHeight();
        }
    }
}
