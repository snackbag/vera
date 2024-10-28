package app;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;

public class TestApplication extends VeraApp {
    public TestApplication() {
        // Don't expect the provider to exist
        super(Vera.getProvider("test"));
    }

    @Override
    public void init() {

    }
}
