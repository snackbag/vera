package net.snackbag.mcvera.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.screen.VeraVisibilityScreen;
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
		if (screen == null && !MCVeraData.visibleApplications.isEmpty()) {
			if (!(MinecraftClient.getInstance().currentScreen instanceof VeraVisibilityScreen)) {
				setScreen(new VeraVisibilityScreen());
			}
		} else if (screen instanceof VeraVisibilityScreen && MCVeraData.visibleApplications.isEmpty()) {
			setScreen(null);
		}
	}
}