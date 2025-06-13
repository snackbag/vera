package net.snackbag.mcvera.test;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TestHandler {
    public static boolean shouldTest() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static void impl(boolean force) {
        if (!(force || shouldTest())) return;

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_APOSTROPHE)) {
                if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                    StyleTestApplication.INSTANCE.show();
                } else {
                    TestApplication.INSTANCE.setVisibility(true);
                }
            }
        });
    }
}
