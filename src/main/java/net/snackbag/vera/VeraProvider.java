package net.snackbag.vera;

import net.snackbag.vera.core.VeraApp;

public interface VeraProvider {
    void handleAppInitialization(VeraApp app);

    void renderApp(VeraApp app);

    int getScreenHeight();

    int getScreenWidth();
}
