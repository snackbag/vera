package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VImage;
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
        label.setPadding(0);
        label.setBackgroundColor(VColor.black());
        label.setFont(label.getFont().withColor(VColor.white()));
        label.adjustSize();
        addWidget(label);

        VImage image = new VImage(
                Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png").toString(),
                32, 32, this);
        image.move(0, 30);
        addWidget(image);
    }
}
