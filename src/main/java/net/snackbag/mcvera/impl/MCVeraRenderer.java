package net.snackbag.mcvera.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
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
    public void drawRect(VeraApp app, int x, int y, int width, int height, double rotation, VColor color) {
        drawContext.fill(x, y, x + width, y + height, color.toInt());
    }

    @Override
    public void drawText(VeraApp app, int x, int y, double rotation, String text, VFont font) {
        drawContext.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, font.getColor().toInt(), false);
    }

    @Override
    public void drawImage(VeraApp veraApp, int x, int y, int width, int height, double rotation, String path) {
        drawContext.drawTexture(new Identifier(path), x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void renderApp(VeraApp app) {
        List<VWidget> widgets = app.getWidgets();
        RenderSystem.enableBlend();

        app.render();
        List<VWidget> hoveredWidgets = app.getHoveredWidgets();
        for (VWidget widget : widgets) {
            widget.setHovered(hoveredWidgets.contains(widget));
            widget.render();
        }

        RenderSystem.disableBlend();
    }
}
