package net.snackbag.vera;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VLabel;

public interface VeraProvider {
    void handleAppInitialization(VeraApp app);

    void renderLabel(VLabel label);

    int getScreenHeight();

    int getScreenWidth();
}
