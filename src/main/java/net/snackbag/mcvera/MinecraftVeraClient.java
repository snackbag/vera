package net.snackbag.mcvera;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.mcvera.test.TestHandler;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MinecraftVeraClient implements ClientModInitializer {
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
            String combination = makeCombination(client);

            for (VeraApp app : MCVeraData.visibleApplications) {
                app.handleShortcut(combination);
            }
        });
    }

    private String makeCombination(MinecraftClient client) {
        StringBuilder builder = new StringBuilder();
        Resource resource = client.getResourceManager().getResource(new Identifier(MinecraftVera.MOD_ID, "key_mappings.json")).orElseThrow();
        JsonObject json;

        try {
            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            json = new Gson().fromJson(reader, JsonObject.class);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int key : MCVeraData.pressedKeys) {
            String translationKey = InputUtil.fromKeyCode(key, 0).getTranslationKey().toLowerCase().replace(" ", "");
            String mapped = json.has(translationKey) ?
                    json.get(translationKey).getAsString().toLowerCase().replace(" ", "") :
                    translationKey.replace("key.keyboard.", "");

            builder.append(mapped).append("+");
        }

        if (builder.length() > 1) builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
