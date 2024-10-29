package net.snackbag.mcvera;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.mcvera.test.TestHandler;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

public class MinecraftVeraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftVera.LOGGER.info("Loading client Vera implementation...");

        Vera.registerProvider("mcvera", new MCVeraProvider());
        TestHandler.impl(false);

        ClientTickEvents.END_CLIENT_TICK.register((client) -> renderApplications());
    }

    public void renderApplications() {
        MCVeraRenderer renderer = MCVeraRenderer.getInstance();

        for (VeraApp app : MCVeraData.visibleApplications) {
            renderer.renderApp(app);
        }
    }
}
