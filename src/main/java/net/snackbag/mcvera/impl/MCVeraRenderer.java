package net.snackbag.mcvera.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;

import java.util.List;

public class MCVeraRenderer implements VeraRenderer {
    private static MCVeraRenderer instance = null;
    public static DrawContext drawContext = null;

    public static MCVeraRenderer getInstance() {
        if (instance == null) {
            instance = new MCVeraRenderer();
        }

        return instance;
    }

    @Override
    public void drawRect(VeraApp app, int x, int y, int width, int height, VColor color) {
        drawContext.fill(x, y, width, height, color.toInt());
    }

    @Override
    public void drawText(VeraApp app, int x, int y, String text, VFont font) {
        drawContext.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, font.getColor().toInt(), true);
    }

    @Override
    public void renderApp(VeraApp app) {
        List<VWidget> widgets = app.getWidgets();

        for (VWidget widget : widgets) {
            widget.render();
        }
    }
}
