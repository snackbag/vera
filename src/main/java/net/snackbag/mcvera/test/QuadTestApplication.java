package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;

public class QuadTestApplication extends VeraApp {
    public static QuadTestApplication INSTANCE = new QuadTestApplication();

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);
    }

    @Override
    public void render() {
        Vera.renderer.renderColQuad(
                0, 0,
                0, 100,
                100, 100,
                100, 0,
                VColor.MC_RED
        );

        Vera.renderer.renderTexQuad(
                true,
                new Identifier("mcvera", "icon.png"),
                100, 0,
                120, 100,
                220, 100,
                200, 0
        );
    }
}
