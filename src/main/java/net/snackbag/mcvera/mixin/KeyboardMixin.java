package net.snackbag.mcvera.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
//? if (>=1.21.11) {
/*import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
*///?}
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
    //? if (>=1.21.11) {
    /*private void mcvera$handleKey(long window, int action, KeyInput input, CallbackInfo ci) {
        if (action == 1) MCVeraData.pressedKeys.add(input.key());
        if (action == 0) MCVeraData.pressedKeys.remove((Integer) input.key());
    *///?} else if (>=1.20.1) {
    private void mcvera$handleKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == 1) MCVeraData.pressedKeys.add(key);
        if (action == 0) MCVeraData.pressedKeys.remove((Integer) key);
    //?}
    }

    @Inject(at = @At("HEAD"), method = "onChar")
    //? if (>=1.21.11) {
    /*private void mcvera$handleCharPressed(long window, CharInput input, CallbackInfo ci) {
        if (window == client.getWindow().getHandle()) {
            Vera.provider.handleCharTyped((char) input.codepoint(), input.modifiers());
        }
    *///?} else if (>=1.20.1) {
    private void mcvera$handleCharPressed(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if (window == client.getWindow().getHandle()) {
            Vera.provider.handleCharTyped((char) codePoint, modifiers);
        }
    //?}
    }
}
