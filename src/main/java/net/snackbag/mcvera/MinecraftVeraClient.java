package net.snackbag.mcvera;

import net.fabricmc.api.ClientModInitializer;

public class MinecraftVeraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftVera.LOGGER.info("Loading client Vera implementation...");
    }
}
