package net.snackbag.mcvera.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onCursorPos", at = @At("HEAD"))
    private void mcvera$onCursorMove(long window, double fx, double fy, CallbackInfo ci) {
        if (client.getWindow().getHandle() != window) return;

        double scaleFactor = client.getWindow().getScaleFactor();

        int mouseX = (int) (fx / scaleFactor);
        int mouseY = (int) (fy / scaleFactor);

        VeraApp top = Vera.getTopHierarchyApp();
        Vera.forAllVisibleApps(app -> {
            if (app.isRequiresHierarchy() && app != top) return;

            VWidget<?> widget = app.getTopWidgetAt(mouseX, mouseY);
            if (widget != null) widget.events.fire("mouse-move", mouseX, mouseY);
            else if (app.getCursorShape() != VCursorShape.DEFAULT) app.setCursorShape(VCursorShape.DEFAULT);
        });
    }

    @Inject(method = "onFilesDropped", at = @At("HEAD"))
    private void mcvera$onFilesWindowHoverDropped(long window, List<Path> paths, CallbackInfo ci) {
        Vera.provider.handleFilesDropped(paths);
    }
}
