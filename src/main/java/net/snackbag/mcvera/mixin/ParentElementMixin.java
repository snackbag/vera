package net.snackbag.mcvera.mixin;

//? if (>=1.21.11) {
/*import net.minecraft.client.gui.Click;
*///?}
import net.minecraft.client.gui.ParentElement;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VMouseButton;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VEvents;
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
    //? if (>=1.21.11) {
    /*private void mcvera$handleMouseClick(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) click.x();
        int mouseY = (int) click.y();

        VMouseButton btn = VMouseButton.fromInt(click.getKeycode());
    *///?} else if (>=1.20.1) {
    private void mcvera$handleMouseClick(double mouseXRaw, double mouseYRaw, int button, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;

        VMouseButton btn = VMouseButton.fromInt(button);
    //?}
        boolean justChanged = false;



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
    //? if (>=1.21.11) {
    /*private void mcvera$handleMouseRelease(Click click, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) click.x();
        int mouseY = (int) click.y();

        VMouseButton btn = VMouseButton.fromInt(click.getKeycode());
    *///?} else if (>=1.20.1) {
    private void mcvera$handleMouseRelease(double mouseXRaw, double mouseYRaw, int button, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;

        VMouseButton btn = VMouseButton.fromInt(button);
    //?}

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

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    //? if (1.20.1) {
    private void mcvera$handleMouseScroll(double mouseXRaw, double mouseYRaw, double amount, CallbackInfoReturnable<Boolean> cir) {
    //?} else if (>=1.21.1) {
    /*private void mcvera$handleMouseScroll(double mouseXRaw, double mouseYRaw, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
    *///?}
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;

        // I'm not sure what to do with this. Horizontal scrolling support was added in 1.21.1, and we should probably support it in Vera too, but for now it's just vertical -Lemonnik
        //? if (>=1.21.1)
        //double amount = verticalAmount;

        MCVeraData.asTopHierarchy(app -> handleScrollEvents(app.getTopWidgetAt(mouseX, mouseY), mouseX, mouseY, amount));
        Vera.forAllVisibleApps(app -> {
            if (app.hasFlag(VAppFlag.HIERARCHIC)) return;
            if (!app.isPointOverThis(mouseX, mouseY)) return;

            handleScrollEvents(app.getTopWidgetAt(mouseX, mouseY), mouseX, mouseY, amount);
        });
    }

    @Unique
    private void handleScrollEvents(@Nullable VWidget<?> widget, int x, int y, double amount) {
        if (widget == null) return;
        widget.events.fire(VEvents.Widget.SCROLL, x, y, amount);
    }
}
