package net.snackbag.vera;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;

public interface VeraRenderer {
    void drawRect(VeraApp app, int x, int y, int width, int height, double rotation, VColor color);

    void drawText(VeraApp app, int x, int y, double rotation, String text, VFont font);

    void renderApp(VeraApp app);

    void drawImage(VeraApp app, int x, int y, int width, int height, double rotation, String path);
}
