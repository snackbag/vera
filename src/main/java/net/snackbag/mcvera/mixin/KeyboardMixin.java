package net.snackbag.mcvera.mixin;

import net.minecraft.client.Keyboard;
import net.snackbag.mcvera.MinecraftVeraClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(at = @At("HEAD"), method = "onKey")
    private void mcvera$handleKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == 1) MinecraftVeraClient.pressedKeys.add(key);
        if (action == 0) MinecraftVeraClient.pressedKeys.remove((Integer) key);
    }
}
