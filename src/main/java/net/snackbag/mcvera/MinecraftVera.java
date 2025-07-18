package net.snackbag.mcvera;

import net.fabricmc.api.ModInitializer;

import net.snackbag.vera.Vera;
import net.snackbag.vera.style.standard.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftVera implements ModInitializer {
	public static final String MOD_ID = "mcvera";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading Vera...");

		LOGGER.info("Registering standard styles");
		// sorted by "complicatedness" & importance
		Vera.registrar.registerStandardStyle(new WidgetStandardStyle());

		Vera.registrar.registerStandardStyle(new RectStandardStyle());
		Vera.registrar.registerStandardStyle(new ImageStandardStyle());
		Vera.registrar.registerStandardStyle(new CheckBoxStandardStyle());
		Vera.registrar.registerStandardStyle(new LabelStandardStyle());
		Vera.registrar.registerStandardStyle(new DropdownStandardStyle());
		Vera.registrar.registerStandardStyle(new TabWidgetStandardStyle());
		Vera.registrar.registerStandardStyle(new LineInputStandardStyle());
	}
}