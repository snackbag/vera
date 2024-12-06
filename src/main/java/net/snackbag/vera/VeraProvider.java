package net.snackbag.vera;

import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;

public interface VeraProvider {
    void handleAppInitialization(VeraApp app);

    void handleAppShow(VeraApp app);

    void handleAppHide(VeraApp app);

    default void handleAppVisibilityChange(VeraApp app, boolean visibility) {
        if (visibility) handleAppShow(app);
        else handleAppHide(app);
    }

    void handleRunShortcut(VShortcut shortcut);

    int getScreenHeight();

    int getScreenWidth();

    VeraRenderer getRenderer();

    int getTextWidth(String text, VFont font);

    int getTextHeight(String text, VFont font);

    int getMouseX();

    int getMouseY();

    String getDefaultFontName();
}
