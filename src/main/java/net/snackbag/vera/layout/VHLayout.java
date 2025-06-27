package net.snackbag.vera.layout;

import net.snackbag.vera.core.VeraApp;

public class VHLayout extends VLayout {
    public VHLayout(VeraApp app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

    public VHLayout(VeraApp app, int x, int y) {
        this(app, x, y, -1, -1);
    }
}
