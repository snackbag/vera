package net.snackbag.mcvera.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V", shift = At.Shift.AFTER, ordinal = 0), method = "render")
    private void mcvera$renderBelowVignette(DrawContext context, float tickDelta, CallbackInfo ci) {
        LinkedHashSet<VeraApp> apps = MCVeraData.visibleApplications.getOrDefault(VWindowPositioningFlag.BELOW_VIGNETTE, new LinkedHashSet<>());

        for (VeraApp app : apps) {
            Vera.renderer.renderApp(app);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F"), method = "render")
    private void mcvera$renderBelowOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
        LinkedHashSet<VeraApp> apps = MCVeraData.visibleApplications.getOrDefault(VWindowPositioningFlag.BELOW_OVERLAYS, new LinkedHashSet<>());

        for (VeraApp app : apps) {
            Vera.renderer.renderApp(app);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;getCurrentGameMode()Lnet/minecraft/world/GameMode;", ordinal = 0, shift = At.Shift.BEFORE), method = "render")
    private void mcvera$renderBelowHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        LinkedHashSet<VeraApp> apps = MCVeraData.visibleApplications.getOrDefault(VWindowPositioningFlag.BELOW_HUD, new LinkedHashSet<>());

        for (VeraApp app : apps) {
            Vera.renderer.renderApp(app);
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "renderHotbar")
    private void mcvera$renderHud(float tickDelta, DrawContext context, CallbackInfo ci) {
        LinkedHashSet<VeraApp> apps = MCVeraData.visibleApplications.getOrDefault(VWindowPositioningFlag.HUD, new LinkedHashSet<>());

        for (VeraApp app : apps) {
            Vera.renderer.renderApp(app);
        }
    }
}
