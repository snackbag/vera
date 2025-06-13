package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VLabel;

public class StyleTestApplication extends VeraApp {
    public static final StyleTestApplication INSTANCE = new StyleTestApplication();

    @Override
    public void init() {
        //loadStyleSheet(new Identifier(MinecraftVera.MOD_ID, "demo/test.vss"));

        new VLabel("helo", this).alsoAdd();
    }
}
