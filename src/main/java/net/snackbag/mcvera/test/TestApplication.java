package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.*;

public class TestApplication extends VeraApp {
    public static final TestApplication INSTANCE = new TestApplication();

    public TestApplication() {
        super();
    }

    @Override
    public void init() {
        VShortcut exit = new VShortcut(this, "escape", () -> {
            if (hasFocusedWidget()) {
                setFocusedWidget(null);
                return;
            }

            this.hide();
        });

        VShortcut changeMouseRequired = new VShortcut(this, "leftalt+m", () -> {
            setMouseRequired(!isMouseRequired());
        });

        addShortcut(exit);
        addShortcut(changeMouseRequired);

        VLineInput input = new VLineInput(this);
        input.setMaxChars(15);
        input.setPlaceholderText("Enter text...");
        input.onAddCharLimited(System.out::println);
        input.onMouseMove((x, y) -> System.out.println("x=" + x + ", y=" + y));

        input.move(50);
        input.setBackgroundColor(VColor.white());
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
                Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png"),
                32, 32, this);
        image.move(0, 30);
        addWidget(image);

        VDropdown dropdown = new VDropdown(this);
        dropdown.addItem("coolio");
        dropdown.addItem("shmoolio");
        dropdown.addItem("roolio");
        dropdown.addItem("buger");
        dropdown.move(90, 90);
        addWidget(dropdown);
    }

    @Override
    public void update() {
        super.update();

        move(0);
        setHeight(Vera.provider.getScreenHeight());
        setWidth(Vera.provider.getScreenWidth());
    }
}
