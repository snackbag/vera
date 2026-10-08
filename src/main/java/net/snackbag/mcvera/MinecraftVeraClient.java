package net.snackbag.mcvera;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.snackbag.mcvera.test.TestHandler;
import net.snackbag.vera.Vera;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MinecraftVeraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftVera.LOGGER.info("Loading client Vera implementation...");
        TestHandler.impl(false);

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            // only when changing
            if (MCVeraData.previousPressedKeys.equals(MCVeraData.pressedKeys)) return;
            MCVeraData.previousPressedKeys = new ArrayList<>(MCVeraData.pressedKeys);

            String combination = makeCombination(client);
            Vera.forVisibleAndAllowedApps(app -> app.handleShortcut(combination));
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
