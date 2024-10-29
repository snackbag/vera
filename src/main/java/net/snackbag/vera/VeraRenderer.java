package net.snackbag.vera;

import net.snackbag.vera.core.VFont;

public interface VeraRenderer {
    void drawRect(int x, int y, int width, int height);

    void drawText(int x, int y, VFont font);
}
