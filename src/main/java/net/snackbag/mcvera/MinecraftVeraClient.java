package net.snackbag.mcvera;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.mcvera.test.TestHandler;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

import java.util.ArrayList;
import java.util.List;

public class MinecraftVeraClient implements ClientModInitializer {
    public final static List<Integer> pressedKeys = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        MinecraftVera.LOGGER.info("Loading client Vera implementation...");

        Vera.registerProvider("mcvera", new MCVeraProvider());
        TestHandler.impl(false);

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MCVeraRenderer.drawContext = context;
            MCVeraRenderer renderer = MCVeraRenderer.getInstance();

            for (VeraApp app : MCVeraData.visibleApplications) {
                renderer.renderApp(app);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            String combination = makeCombination();

            for (VeraApp app : MCVeraData.visibleApplications) {
                app.handleShortcut(combination);
            }
        });
    }

    private String makeCombination() {
        StringBuilder builder = new StringBuilder();

        for (int key : pressedKeys) {
            builder
                    .append(InputUtil.fromKeyCode(key, 0).getLocalizedText().getString().toLowerCase().replace(" ", ""))
                    .append("+");
        }

        if (builder.length() > 1) builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
