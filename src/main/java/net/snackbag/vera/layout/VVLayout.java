package net.snackbag.vera.layout;

import net.snackbag.vera.core.VeraApp;

public class VVLayout extends VLayout {
    public VVLayout(VeraApp app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VVLayout(VeraApp app, int x, int y) {
        this(app, x, y, -1, -1);
    }
}
