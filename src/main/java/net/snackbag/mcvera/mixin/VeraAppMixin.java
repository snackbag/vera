package net.snackbag.mcvera.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.vera.core.VeraApp;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VeraApp.class)
public abstract class VeraAppMixin {
    @Inject(at = @At("HEAD"), method = "handleShortcut", remap = false)
    private void mcvera$handleShortcut(String combination, CallbackInfo ci) {
        VeraApp instance = (VeraApp) (Object) this;
        if (!MCVeraData.debugApps.contains(instance)) return;

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§e§l[DEBUG]§f: (shortcut) " + combination));
    }
}
