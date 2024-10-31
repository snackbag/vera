package net.snackbag.mcvera.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.screen.VeraVisibilityScreen;
import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VWidget;

public class MCVeraProvider implements VeraProvider {
    @Override
    public void handleAppInitialization(VeraApp app) {
        MCVeraData.applications.add(app);
        MinecraftClient.getInstance().send(app::init);
        MinecraftClient.getInstance().send(app::update);

        app.addShortcut(new VShortcut(app, "LeftCtrl+LeftAlt+LeftShift+D", () -> {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Debug mode enabled"));
            MCVeraData.debugApps.add(app);
        }, false));
    }

    @Override
    public void handleAppShow(VeraApp app) {
        MCVeraData.visibleApplications.add(app);
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(app::update);

        for (VWidget widget : app.getWidgets()) {
            client.send(widget::update);
        }

        if (client.currentScreen == null) client.setScreen(new VeraVisibilityScreen());
    }

    @Override
    public void handleAppHide(VeraApp app) {
        MCVeraData.visibleApplications.remove(app);
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(app::update);

        for (VWidget widget : app.getWidgets()) {
            client.send(widget::update);
        }

        if (MCVeraData.visibleApplications.isEmpty() && client.currentScreen instanceof VeraVisibilityScreen) {
            client.setScreen(null);
        }
    }

    @Override
    public void handleRunShortcut(VShortcut shortcut) {
        MinecraftClient.getInstance().send(shortcut.getEvent());
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
        return MCVeraRenderer.getInstance();
    }

    @Override
    public int getTextWidth(String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        return (int) (MinecraftClient.getInstance().textRenderer.getWidth(text) * scaleFactor);
    }

    @Override
    public int getTextHeight(String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        return (int) (MinecraftClient.getInstance().textRenderer.fontHeight * scaleFactor);
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
