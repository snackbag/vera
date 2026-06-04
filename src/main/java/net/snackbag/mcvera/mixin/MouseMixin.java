package net.snackbag.mcvera.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VWidgetEvent;
import net.snackbag.vera.flag.VAppFlag;
import net.snackbag.vera.util.DragHandler;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

        VeraApp top = MCVeraData.getTopHierarchy();
        Vera.forAllVisibleApps(app -> {
            if (app.hasFlag(VAppFlag.HIERARCHIC) && app != top) return;

            VWidget<?> widget = app.getTopWidgetAt(mouseX, mouseY);
            if (widget != null) widget.events.fire(new VWidgetEvent.MouseMove(mouseX, mouseY));
            else if (app.getCursorShape() != VCursorShape.DEFAULT) app.setCursorShape(VCursorShape.DEFAULT);
        });

        DragHandler.move();
    }

    @Inject(method = "onFilesDropped", at = @At("HEAD"))
    private void mcvera$onFilesWindowHoverDropped(long window, List<Path> paths, CallbackInfo ci) {
        Vera.provider.handleFilesDropped(paths);
    }

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;"))
    private void mcvera$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci, @Local(ordinal = 2) double d) {
        // Note for good measure: vanilla doesn't apply sensitivity to horizontal, but only vertical. Therefore, we
        // use d for vertical and horizontal for horizontal

        int mouseX = Vera.getMouseX();
        int mouseY = Vera.getMouseY();

        MCVeraData.asTopHierarchy(app -> handleScrollEvents(app.getTopWidgetAt(mouseX, mouseY), horizontal, d));
        Vera.forAllVisibleApps(app -> {
            if (app.hasFlag(VAppFlag.HIERARCHIC)) return;
            if (!app.isPointOverThis(mouseX, mouseY)) return;

            handleScrollEvents(app.getTopWidgetAt(mouseX, mouseY), horizontal, d);
        });
    }

    @Unique
    private void handleScrollEvents(@Nullable VWidget<?> widget, double horizontal, double vertical) {
        if (widget == null) return;
        widget.events.fire(new VWidgetEvent.MouseScroll(horizontal, vertical));
    }
}
