package net.snackbag.mcvera.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.screen.VeraVisibilityScreen;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VWidget;

public class MCVeraProvider {
    public void handleAppInitialization(VeraApp app) {
        MCVeraData.applications.add(app);
        MinecraftClient.getInstance().send(app::init);
        MinecraftClient.getInstance().send(app::update);

        app.addShortcut(new VShortcut(app, "LeftCtrl+LeftAlt+LeftShift+D", () -> {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Debug mode enabled"));
            MCVeraData.debugApps.add(app);
        }, false));
    }

    public void handleAppShow(VeraApp app) {
        MCVeraData.visibleApplications.add(app);
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(app::update);

        for (VWidget widget : app.getWidgets()) {
            client.send(widget::update);
        }

        handleMouseLockUpdate();
    }

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

    public void handleAppVisibilityChange(VeraApp app, boolean visibility) {
        if (visibility) handleAppShow(app);
        else handleAppHide(app);
    }

    public void handleMouseLockUpdate() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (MCVeraData.appsWithMouseRequired <= 0 && client.currentScreen instanceof VeraVisibilityScreen)
            client.setScreen(null);

        if (MCVeraData.appsWithMouseRequired >= 0 && client.currentScreen == null)
            client.setScreen(new VeraVisibilityScreen());
    }

    public void handleRunShortcut(VShortcut shortcut) {
        MinecraftClient.getInstance().send(shortcut.getEvent());
    }

    public int getScreenHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    public int getScreenWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public int getTextWidth(String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        return (int) (MinecraftClient.getInstance().textRenderer.getWidth(text) * scaleFactor);
    }

    public int getTextHeight(String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        return (int) (MinecraftClient.getInstance().textRenderer.fontHeight * scaleFactor);
    }

    public int getMouseX() {
        return (int) (MinecraftClient.getInstance().mouse.getX() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    public int getMouseY() {
        return (int) (MinecraftClient.getInstance().mouse.getY() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    public void handleKeyPressed(int keyCode, int scanCode, int modifiers) {
        for (VeraApp app : MCVeraData.visibleApplications) {
            app.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void handleCharTyped(char chr, int modifiers) {
        for (VeraApp app : MCVeraData.visibleApplications) {
            app.charTyped(chr, modifiers);
        }
    }

    public String getDefaultFontName() {
        return "minecraft:default";
    }
}
