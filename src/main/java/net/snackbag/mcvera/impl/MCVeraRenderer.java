package net.snackbag.mcvera.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;

import java.util.List;

public class MCVeraRenderer {
    public static DrawContext drawContext = null;

    public void drawRect(VeraApp app, int x, int y, int width, int height, double rotation, VColor color) {
        MatrixStack stack = drawContext.getMatrices();
        stack.push();

        int centerX = x + width / 2;
        int centerY = y + height / 2;

        stack.translate(app.getX(), app.getY(), 0);
        stack.translate(centerX, centerY, 0);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) rotation));
        stack.translate(-width / 2f, -height / 2f, 0);

        drawContext.fill(0, 0, width, height, color.toInt());

        stack.pop();
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
