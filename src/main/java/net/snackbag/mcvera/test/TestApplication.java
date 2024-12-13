package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.VImage;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VLineInput;

public class TestApplication extends VeraApp {
    public static final TestApplication INSTANCE = new TestApplication();

    public TestApplication() {
        super();
    }

    @Override
    public void init() {
        VShortcut shortcut = new VShortcut(this, "LeftWin+N", () -> {
            System.out.println("Shortcut pressed!");
        });
        addShortcut(shortcut);

        setBackgroundColor(VColor.white());

        VLineInput input = new VLineInput(this);
        input.move(50);
        addWidget(input);
        setFocusedWidget(input);

        VLabel label = new VLabel("Hello world!", this);
        label.setPadding(5);
        label.move(10);
        label.setBackgroundColor(VColor.black());
        label.setFont(label.getFont().withColor(VColor.white()));
        label.adjustSize();
        label.onHover(() -> {
            label.setText("Hovered");
        });

        label.onHoverLeave(() -> {
            label.setText("Not hovered");
        });
        addWidget(label);

        VImage image = new VImage(
                Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png").toString(),
                32, 32, this);
        image.move(0, 30);
        addWidget(image);
    }

    @Override
    public void update() {
        super.update();

        move(0);
        setHeight(20);
        setWidth(Vera.provider.getScreenWidth());
    }
}
