package net.snackbag.mcvera.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.core.VeraApp;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(at = @At("TAIL"), method = "render")
    private void mcvera$renderUI(DrawContext context, float tickDelta, CallbackInfo ci) {
        MCVeraRenderer renderer = MCVeraRenderer.getInstance();
        MCVeraRenderer.drawContext = context;

        for (VeraApp app : MCVeraData.visibleApplications) {
            renderer.renderApp(app);
        }
    }
}
