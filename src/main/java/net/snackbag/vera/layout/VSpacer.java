package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.core.VAppAccess;

public class VSpacer extends VElement {
    public VSpacer(VAppAccess app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VSpacer(VAppAccess app, int width, int height) {
        this(app, 0, 0, width, height);
    }
}
