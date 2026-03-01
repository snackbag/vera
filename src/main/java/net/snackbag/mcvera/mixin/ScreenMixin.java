package net.snackbag.mcvera.mixin;

import net.minecraft.client.gui.screen.Screen;
//? if (>=1.21.11) {
/*import net.minecraft.client.input.KeyInput;
*///?}
import net.snackbag.vera.Vera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    //? if (>=1.21.11) {
    /*private void mcvera$handleKeyPress(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        Vera.provider.handleKeyPressed(input.getKeycode(), input.scancode(), input.modifiers());
    *///?} else if (>=1.20.1) {
    private void mcvera$handleKeyPress(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Vera.provider.handleKeyPressed(keyCode, scanCode, modifiers);
    //?}
    }
}
