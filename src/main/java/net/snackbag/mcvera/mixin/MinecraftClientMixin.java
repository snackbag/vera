package net.snackbag.mcvera.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.screen.VeraVisibilityScreen;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow public abstract void setScreen(@Nullable Screen screen);

	@Inject(at = @At("HEAD"), method = "setScreen")
	private void mcvera$veraVisibilityManager(@Nullable Screen screen, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		client.send(() -> {
			if (screen == null && MCVeraData.appsWithMouseRequired >= 1) {
				if (!(MinecraftClient.getInstance().currentScreen instanceof VeraVisibilityScreen)) {
					setScreen(new VeraVisibilityScreen());
				}
			} else if (screen instanceof VeraVisibilityScreen && MCVeraData.appsWithMouseRequired <= 0) {
				setScreen(null);
			}
		});
	}

	@Inject(at = @At("HEAD"), method = "onResolutionChanged")
	private void mcvera$handleResize(CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		for (VeraApp app : MCVeraData.visibleApplications) {
			client.send(app::update);
			for (VWidget<?> widget : app.getWidgets()) {
				client.send(widget::update);
			}
		}
	}
}