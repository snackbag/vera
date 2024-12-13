package net.snackbag.mcvera.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "onKey")
    private void mcvera$handleKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == 1) MCVeraData.pressedKeys.add(key);
        if (action == 0) MCVeraData.pressedKeys.remove((Integer) key);
    }

    @Inject(at = @At("HEAD"), method = "onChar")
    private void mcvera$handleCharPressed(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if (window == client.getWindow().getHandle()) {
            Vera.provider.handleCharTyped((char) codePoint, modifiers);
        }
    }
}
