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
        label.setPadding(5);
        label.setBackgroundColor(VColor.black());
        label.setFont(label.getFont().withColor(VColor.white()));
        addWidget(label);

        VImage image = new VImage(
                Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png").toString(),
                512, 512, this);
        image.move(30);
        addWidget(image);
    }
}
