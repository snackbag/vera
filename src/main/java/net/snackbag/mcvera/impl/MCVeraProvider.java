package net.snackbag.mcvera.impl;

import net.minecraft.client.MinecraftClient;
import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;

public class MCVeraProvider implements VeraProvider {
    @Override
    public void handleAppInitialization(VeraApp veraApp) {

    }

    @Override
    public void renderApp(VeraApp veraApp) {

    }

    @Override
    public int getScreenHeight() {
        return MinecraftClient.getInstance().getWindow().getHeight();
    }

    @Override
    public int getScreenWidth() {
        return MinecraftClient.getInstance().getWindow().getWidth();
    }

    @Override
    public VeraRenderer getRenderer() {
        return ;
    }

    @Override
    public int getTextWidth(String text, VFont font) {
        return font.getSize();
    }

    @Override
    public int getTextHeight(String text, VFont font) {
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    @Override
    public int getMouseX() {
        return (int) (MinecraftClient.getInstance().mouse.getX() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    @Override
    public int getMouseY() {
        return (int) (MinecraftClient.getInstance().mouse.getY() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    @Override
    public String getDefaultFontName() {
        return "minecraft:default";
    }
}
