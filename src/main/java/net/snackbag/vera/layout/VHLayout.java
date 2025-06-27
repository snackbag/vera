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

    @Override
    public void rebuild() {
        int x = getX();

        for (VElement elem : elements) {
            cache.put(elem, new Vector2i(x, getY()));

            x += elem.getWidth();
        }
    }
}
