package net.snackbag.mcvera.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.snackbag.vera.Vera;
import net.snackbag.vera.flag.VAppPositioningFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to hook Vera rendering into Minecraft's HUD render pipeline
 * <br><br>
 * <strong>In >=1.21.1</strong> a general {@code render(...)} method in {@link InGameHud} doesn't exist anymore.
 * <br>
 * Because of that: <br>
 * {@link #mcvera$beginRender(DrawContext, RenderTickCounter, CallbackInfo)} <br>
 * and <br>
 * {@link #mcvera$renderBelowVignette(DrawContext, RenderTickCounter, CallbackInfo)} <br>
 * </ul>
 * both inject at the <strong>HEAD</strong> of {@code renderMiscOverlays(DrawContext, RenderTickCounter)}
 * as it's the first method to be called in the HUD render pipeline
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {


    //? if (1.20.1) {
    @Inject(at = @At(value = "HEAD"), method = "render")
    private void mcvera$beginRender(DrawContext context, float tickDelta, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*@Inject(at = @At(value = "HEAD"), method = "renderMiscOverlays")
    private void mcvera$beginRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    *///?}
        Vera.renderCacheId = System.currentTimeMillis();
    }

    //? if (1.20.1) {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V", shift = At.Shift.AFTER, ordinal = 0, remap = false), method = "render")
    private void mcvera$renderBelowVignette(DrawContext context, float tickDelta, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*@Inject(at = @At("HEAD"), method = "renderMiscOverlays")
    private void mcvera$renderBelowVignette(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.BELOW_VIGNETTE);
    }

    //? if (>=1.21.11) {
    /*@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderTickCounter;getDynamicDeltaTicks()F"), method = "renderMiscOverlays")
    private void mcvera$renderBelowOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    *///?} else if (>=1.21.1) {
    /*@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderTickCounter;getLastFrameDuration()F"), method = "renderMiscOverlays")
    private void mcvera$renderBelowOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    *///?} else if (1.20.1) {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F"), method = "render")
    private void mcvera$renderBelowOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
    //?}
        Vera.renderer.renderApps(VAppPositioningFlag.BELOW_OVERLAYS);
    }

    //? if (1.20.1) {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;getCurrentGameMode()Lnet/minecraft/world/GameMode;", ordinal = 0, shift = At.Shift.BEFORE), method = "render")
    private void mcvera$renderBelowHud(DrawContext context, float tickDelta, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*@Inject(at = @At(value = "HEAD"), method = "renderMainHud")
    private void mcvera$renderBelowHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.BELOW_HUD);
    }

    @Inject(at = @At(value = "TAIL"), method = "renderHotbar")
    //? if (1.20.1) {
    private void mcvera$renderHud(float tickDelta, DrawContext context, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*private void mcvera$renderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.HUD);
    }
}
