package net.snackbag.vera;

import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;

public interface VeraProvider {
    void handleAppInitialization(VeraApp app);

    void renderApp(VeraApp app);

    int getScreenHeight();

    int getScreenWidth();

    VeraRenderer getRenderer();

    int getTextWidth(String text, VFont font);

    int getTextHeight(String text, VFont font);

    String getDefaultFontName();
}
