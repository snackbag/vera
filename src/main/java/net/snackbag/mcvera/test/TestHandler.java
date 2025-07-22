package net.snackbag.mcvera.test;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.snackbag.mcvera.InternalCommands;

public class TestHandler {
    public static boolean shouldTest() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static void impl(boolean force) {
        if (!(force || shouldTest())) return;

        ClientCommandRegistrationCallback.EVENT.register(InternalCommands::register);
    }
}
