package net.snackbag.mcvera.mixin;

import net.minecraft.client.gui.ParentElement;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VMouseButton;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VWidgetEvent;
import net.snackbag.vera.flag.VAppFlag;
import net.snackbag.vera.util.DragHandler;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParentElement.class)
public interface ParentElementMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mcvera$handleMouseClick(double mouseXRaw, double mouseYRaw, int button, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;
        boolean justChanged = false;

        VMouseButton btn = VMouseButton.fromInt(button);

        List<VeraApp> hierarchicApps = new ArrayList<>(MCVeraData.getAppsWithFlag(VAppFlag.HIERARCHIC));
        for (VeraApp app : hierarchicApps) {
            if (app.isPointOverThis(mouseX, mouseY)) {
                if (MCVeraData.isTopHierarchy(app)) break;

                app.moveToHierarchyTop();
                justChanged = true;
                break;
            }
        }

        boolean finalJustChanged = justChanged; // weird java shit
        MCVeraData.asTopHierarchy(app -> {
            if (!app.isPointOverThis(mouseX, mouseY)) return;
            if (finalJustChanged) return;

            handleClickEvents(app.getTopWidgetAt(mouseX, mouseY), btn);
        });

        Vera.forAllVisibleApps(app -> {
            if (app.hasFlag(VAppFlag.HIERARCHIC)) return;

            VWidget<?> hoveredWidget = app.getTopWidgetAt(mouseX, mouseY);
            if (hoveredWidget != null) handleClickEvents(hoveredWidget, btn);
            else app.setFocusedWidget(null);
        });
    }

    @Unique
    private void handleClickEvents(@Nullable VWidget<?> widget, VMouseButton button) {
        if (widget == null) return;

        switch (button) {
            case LEFT -> widget.events.fire(VEvents.Widget.LEFT_CLICK);
            case RIGHT -> widget.events.fire(VEvents.Widget.RIGHT_CLICK);
            case MIDDLE -> widget.events.fire(VEvents.Widget.MIDDLE_CLICK);
        }

        DragHandler.down(button, widget);
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void mcvera$handleMouseRelease(double mouseXRaw, double mouseYRaw, int button, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;

        VMouseButton btn = VMouseButton.fromInt(button);

        MCVeraData.asTopHierarchy(app -> handleReleaseEvents(app.getTopWidgetAt(mouseX, mouseY), btn));
        Vera.forAllVisibleApps(app -> {
            if (app.hasFlag(VAppFlag.HIERARCHIC)) return;
            if (!app.isPointOverThis(mouseX, mouseY)) return;

            handleReleaseEvents(app.getTopWidgetAt(mouseX, mouseY), btn);
        });

        DragHandler.release(btn);
    }

    @Unique
    private void handleReleaseEvents(@Nullable VWidget<?> widget, VMouseButton button) {
        if (widget == null) return;

        switch (button) {
            case LEFT -> widget.events.fire(VEvents.Widget.LEFT_CLICK_RELEASE);
            case RIGHT -> widget.events.fire(VEvents.Widget.RIGHT_CLICK_RELEASE);
            case MIDDLE -> widget.events.fire(VEvents.Widget.MIDDLE_CLICK_RELEASE);
        }
    }
}
