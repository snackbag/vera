package net.snackbag.mcvera;

import net.fabricmc.api.ModInitializer;

import net.snackbag.vera.Vera;
import net.snackbag.vera.style.standard.LabelStandardStyle;
import net.snackbag.vera.style.standard.RectStandardStyle;
import net.snackbag.vera.style.standard.WidgetStandardStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftVera implements ModInitializer {
	public static final String MOD_ID = "mcvera";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading Vera...");

		Vera.registrar.registerStandardStyle(new WidgetStandardStyle());
		Vera.registrar.registerStandardStyle(new LabelStandardStyle());
		Vera.registrar.registerStandardStyle(new RectStandardStyle());
	}
}