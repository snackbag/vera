package net.snackbag.mcvera.mixin;

import net.minecraft.client.gui.ParentElement;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParentElement.class)
public interface ParentElementMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mcvera$handleMouseClick(double mouseXRaw, double mouseYRaw, int button, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;
        boolean justChanged = false;

        VeraApp top = MCVeraData.getTopHierarchy();

        for (VeraApp app : MCVeraData.appHierarchy) {
            if (app.isMouseOverApp(mouseX, mouseY) && top != app) {
                app.moveToHierarchyTop();
                justChanged = true;
                break;
            }
        }

        boolean finalJustChanged = justChanged; // weird java shit
        MCVeraData.asTopHierarchy(app -> {
            if (!app.isMouseOverThis(mouseX, mouseY)) return;
            if (!finalJustChanged) return;
            handleClickEvents(app.getHoveredWidget(mouseX, mouseY), button);
        });

        Vera.forAllVisibleApps((app) -> {
            if (app.isRequiresHierarchy()) return;

            VWidget<?> hoveredWidget = app.getHoveredWidget(mouseX, mouseY);
            if (hoveredWidget != null) handleClickEvents(hoveredWidget, button);
            else app.setFocusedWidget(null);
        });
    }

    @Unique
    private void handleClickEvents(@Nullable VWidget<?> widget, int button) {
        if (widget == null) return;

        switch (button) {
            case 0 -> widget.fireEvent("left-click");
            case 1 -> widget.fireEvent("right-click");
            case 2 -> widget.fireEvent("middle-click");
            default -> throw new IllegalArgumentException("Invalid button type: %d".formatted(button));
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void mcvera$handleMouseRelease(double mouseXRaw, double mouseYRaw, int button, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;

        MCVeraData.asTopHierarchy(app -> handleReleaseEvents(app.getHoveredWidget(mouseX, mouseY), button));
        Vera.forAllVisibleApps(app -> {
            if (app.isRequiresHierarchy()) return;
            handleReleaseEvents(app.getHoveredWidget(mouseX, mouseY), button);
        });
    }

    @Unique
    private void handleReleaseEvents(@Nullable VWidget<?> widget, int button) {
        if (widget == null) return;

        switch (button) {
            case 0 -> widget.fireEvent("left-click-release");
            case 1 -> widget.fireEvent("right-click-release");
            case 2 -> widget.fireEvent("middle-click-release");
            default -> throw new IllegalArgumentException("Invalid button type: %d".formatted(button));
        }
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mcvera$handleMouseScroll(double mouseXRaw, double mouseYRaw, double amount, CallbackInfoReturnable<Boolean> cir) {
        int mouseX = (int) mouseXRaw;
        int mouseY = (int) mouseYRaw;

        MCVeraData.asTopHierarchy(app -> handleScrollEvents(app.getHoveredWidget(mouseX, mouseY), mouseX, mouseY, amount));
        Vera.forAllVisibleApps(app -> {
            if (app.isRequiresHierarchy()) return;
            handleScrollEvents(app.getHoveredWidget(mouseX, mouseY), mouseX, mouseY, amount);
        });
    }

    @Unique
    private void handleScrollEvents(@Nullable VWidget<?> widget, int x, int y, double amount) {
        if (widget == null) return;
        widget.fireEvent("mouse-scroll", x, y, amount);
    }
}
