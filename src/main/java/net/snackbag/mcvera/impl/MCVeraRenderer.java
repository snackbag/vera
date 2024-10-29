package net.snackbag.mcvera.impl;

import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;

public class MCVeraRenderer implements VeraRenderer {
    private static MCVeraRenderer instance = null;

    public static MCVeraRenderer getInstance() {
        if (instance == null) {
            instance = new MCVeraRenderer();
        }

        return instance;
    }

    @Override
    public void drawRect(VeraApp app, int x, int y, int width, int height, VColor color) {

    }

    @Override
    public void drawText(VeraApp app, int x, int y, String text, VFont font) {

    }

    @Override
    public void renderApp(VeraApp app) {

    }
}
