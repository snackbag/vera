package net.snackbag.mcvera;

import net.fabricmc.api.ClientModInitializer;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.vera.Vera;

public class MinecraftVeraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftVera.LOGGER.info("Loading client Vera implementation...");

        Vera.registerProvider("mcvera", new MCVeraProvider());
    }
}
