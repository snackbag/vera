package net.snackbag.mcvera.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
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
        drawContext.fill(x + app.getX(), y + app.getY(), x + width, y + height, color.toInt());
//        drawText(app, x + app.getX(), y < 12 ? y + app.getY() : y - 6 + app.getY(), 0, String.valueOf(color.opacity()), new VFont(Vera.provider.getDefaultFontName(), 12, VColor.of(255, 80, 80)));
    }

    public void drawText(VeraApp app, int x, int y, double rotation, String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x + app.getX(), y + app.getY(), 0);
        drawContext.getMatrices().scale(scaleFactor, scaleFactor, 1.0f);

        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(text).setStyle(Style.EMPTY.withFont(new Identifier(font.getName()))),
                0, 0, // x and y are handled by translate
                font.getColor().toInt(),
                false
        );

        drawContext.getMatrices().pop();
    }

    public void drawImage(VeraApp app, int x, int y, int width, int height, double rotation, Identifier path) {
        drawContext.drawTexture(path, x + app.getX(), y + app.getY(), 0, 0, width, height, width, height);
    }

    public void renderApp(VeraApp app) {
        List<VWidget<?>> widgets = app.getWidgets();
        RenderSystem.enableBlend();

        app.render();
        List<VWidget<?>> hoveredWidgets = app.getHoveredWidgets();
        for (VWidget<?> widget : widgets) {
            widget.setHovered(hoveredWidgets.contains(widget));
            if (widget.visibilityConditionsPassed()) {
                widget.render();
                widget.renderBorder();
            }
        }
        app.renderAfterWidgets();

        RenderSystem.disableBlend();
    }
}
