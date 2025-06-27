package net.snackbag.mcvera.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.screen.VeraVisibilityScreen;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VWidget;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        if (app.isVisible()) return;

        MCVeraData.visibleApplications.get(app.getPositioning()).add(app);
        if (app.isMouseRequired()) MCVeraData.appsWithMouseRequired += 1;
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(app::update);

        for (VWidget<?> widget : app.getWidgets()) {
            client.send(widget::update);
        }

        handleMouseLockUpdate();
    }

    public void handleAppHide(VeraApp app) {
        if (!app.isVisible()) return;

        if (app.isMouseRequired()) MCVeraData.appsWithMouseRequired -= 1;
        MCVeraData.visibleApplications.get(app.getPositioning()).remove(app);
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(app::update);

        for (VWidget<?> widget : app.getWidgets()) {
            client.send(widget::update);
        }

        handleMouseLockUpdate();
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
        Vera.forVisibleAndAllowedApps(app -> app.keyPressed(keyCode, scanCode, modifiers));
    }

    public void handleCharTyped(char chr, int modifiers) {
        Vera.forVisibleAndAllowedApps(app -> app.charTyped(chr, modifiers));
    }

    public String getDefaultFontName() {
        return "minecraft:default";
    }

    public void handleAppSetMouseRequired(VeraApp app, boolean mouseRequired) {
        if (!app.isVisible()) return;

        if (mouseRequired) {
            MCVeraData.appsWithMouseRequired += 1;
        } else {
            MCVeraData.appsWithMouseRequired -= 1;
        }

        handleMouseLockUpdate();
    }

    public void handleFilesDropped(List<Path> paths) {
        VeraApp top = MCVeraData.getTopHierarchy();

        int x = Vera.getMouseX();
        int y = Vera.getMouseY();

        if (top != null && top.isPointOverThis(x, y)) {
            VWidget<?> widget = top.getTopWidgetAt(x, y);
            if (widget != null) {
                widget.events.fire("files-dropped", paths);
                return;
            }
        }

        AtomicBoolean didSomething = new AtomicBoolean(false);
        Vera.forAllVisibleApps(app -> {
            if (didSomething.get()) return;
            if (!app.isPointOverThis(x, y)) return;

            VWidget<?> widget = app.getTopWidgetAt(x, y);
            if (widget != null) {
                widget.events.fire("files-dropped", paths);
                didSomething.set(true);
            }
        });
    }
}
