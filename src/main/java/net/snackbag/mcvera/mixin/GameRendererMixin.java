package net.snackbag.mcvera.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.snackbag.vera.InternalVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.flag.VAppPositioningFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", ordinal = 0, shift = At.Shift.BEFORE))
    //? if (1.20.1) {
    private void mcvera$renderAboveHud(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*private void mcvera$renderAboveHud(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.ABOVE_HUD);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    //? if (1.20.1) {
    private void mcvera$renderOnGUI(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*private void mcvera$renderOnGUI(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.GUI);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    //? if (1.20.1) {
    private void mcvera$renderAboveGUI(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*private void mcvera$renderAboveGUI(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.ABOVE_GUI);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    //? if (1.20.1) {
    private void mcvera$renderScreenAndTop(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
    //? } else if (>=1.21.1) {
    /*private void mcvera$renderScreenAndTop(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
    *///?}
        Vera.renderer.renderApps(VAppPositioningFlag.SCREEN);
        Vera.renderer.renderApps(VAppPositioningFlag.TOP);

        List<Runnable> tasks = new ArrayList<>(InternalVera.getScheduledTasks());
        InternalVera.clearScheduledTasks();

        for (Runnable task : tasks) task.run();
    }
}
