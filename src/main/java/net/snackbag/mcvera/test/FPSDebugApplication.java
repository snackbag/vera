package net.snackbag.mcvera.test;

import net.minecraft.client.MinecraftClient;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VAppFlag;

public class FPSDebugApplication extends VeraApp {
    public static FPSDebugApplication INSTANCE = new FPSDebugApplication();

    @Override
    public void init() {
        setFlag(VAppFlag.REQUIRES_MOUSE, false);
    }

    @Override
    public void renderAfterWidgets() {
        VFont font = VFont.create().withSize(16);
        String text = "FPS: " + MinecraftClient.getInstance().getCurrentFps();

        Vera.renderer.drawText(Vera.getScreenWidth() - Vera.provider.getTextWidth(text, font) - 10, 10, text, font);
    }
}
