package net.snackbag.mcvera.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;

import java.util.List;

public class MCVeraRenderer {
    private static MCVeraRenderer instance = null;
    public static DrawContext drawContext = null;

    public static MCVeraRenderer getInstance() {
        if (instance == null) {
            instance = new MCVeraRenderer();
        }

        return instance;
    }

    public void drawRect(VeraApp app, int x, int y, int width, int height, double rotation, VColor color) {
        drawContext.fill(x, y, x + width, y + height, color.toInt());
    }

    public void drawText(VeraApp app, int x, int y, double rotation, String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x, y, 0);
        drawContext.getMatrices().scale(scaleFactor, scaleFactor, 1.0f);

        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                0, 0, // x and y are handled by translate
                font.getColor().toInt(),
                false
        );

        drawContext.getMatrices().pop();
    }

    public void drawImage(VeraApp veraApp, int x, int y, int width, int height, double rotation, String path) {
        drawContext.drawTexture(new Identifier(path), x, y, 0, 0, width, height, width, height);
    }

    public void renderApp(VeraApp app) {
        List<VWidget> widgets = app.getWidgets();
        RenderSystem.enableBlend();

        app.render();
        List<VWidget> hoveredWidgets = app.getHoveredWidgets();
        for (VWidget widget : widgets) {
            widget.setHovered(hoveredWidgets.contains(widget));
            if (widget.isVisible()) widget.render();
        }

        RenderSystem.disableBlend();
    }
}
