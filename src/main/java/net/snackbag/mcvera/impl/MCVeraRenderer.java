package net.snackbag.mcvera.impl;

import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;

public class MCVeraRenderer implements VeraRenderer {
    private static MCVeraRenderer instance = null;

    public static MCVeraRenderer getInstance() {
        if (instance == null) {
            instance = new MCVeraRenderer();
        }

        return instance;
    }

    @Override
    public void drawRect(int x, int y, int width, int height, VColor color) {

    }

    @Override
    public void drawText(int x, int y, String text, VFont font) {

    }
}
