package net.snackbag.mcvera.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.snackbag.vera.Vera;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onCursorPos", at = @At("HEAD"))
    private void mcvera$onCursorMove(long window, double fx, double fy, CallbackInfo ci) {
        if (client.getWindow().getHandle() != window) return;

        double scaleFactor = client.getWindow().getScaleFactor();

        int scaledX = (int) (fx / scaleFactor);
        int scaledY = (int) (fy / scaleFactor);

        Vera.forHoveredWidget(scaledX, scaledY, (widget) -> {
            widget.fireEvent("mouse-move", scaledX, scaledY);
        });
    }
}
