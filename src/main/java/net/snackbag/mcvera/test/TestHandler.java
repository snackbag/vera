package net.snackbag.mcvera.test;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class TestHandler {
    public static boolean shouldTest() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static void impl(boolean force) {
        if (!(force || shouldTest())) return;

        KeyBinding testKeybinding = new KeyBinding(
                "mcvera.opentest",
                GLFW.GLFW_KEY_APOSTROPHE,
                "mcvera.testing"
        );
        KeyBindingHelper.registerKeyBinding(testKeybinding);

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (testKeybinding.wasPressed()) TestApplication.INSTANCE.setVisibility(!TestApplication.INSTANCE.isVisible());
        });
    }
}
