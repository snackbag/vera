package net.snackbag.mcvera.test;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VLabel;

public class TestApplication extends VeraApp {
    public static final TestApplication INSTANCE = new TestApplication();

    public TestApplication() {
        super(Vera.getProvider("mcvera"));
    }

    @Override
    public void init() {
        setBackgroundColor(VColor.white());
        VLabel label = new VLabel("Hello world!", this);
        addWidget(label);
    }
}
