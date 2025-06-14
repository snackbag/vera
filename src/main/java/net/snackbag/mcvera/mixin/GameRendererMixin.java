package net.snackbag.mcvera.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", ordinal = 0, shift = At.Shift.BEFORE))
    private void mcvera$renderAboveHud(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        Vera.renderer.renderApps(VWindowPositioningFlag.ABOVE_HUD);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private void mcvera$renderOnGUI(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        Vera.renderer.renderApps(VWindowPositioningFlag.GUI);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    private void mcvera$renderAboveGUI(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        Vera.renderer.renderApps(VWindowPositioningFlag.ABOVE_GUI);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    private void mcvera$renderScreenAndTop(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        Vera.renderer.renderApps(VWindowPositioningFlag.SCREEN);
        Vera.renderer.renderApps(VWindowPositioningFlag.TOP);
    }
}
