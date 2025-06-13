package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VAlignmentFlag;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.widget.*;

import java.nio.file.Path;

public class TestApplication extends VeraApp {
    public static final TestApplication INSTANCE = new TestApplication();

    public TestApplication() {
        super();
    }

    @Override
    @SuppressWarnings("removal")
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

        VLineInput input = new VLineInput(this).alsoAdd();
        input.setMaxChars(15);
        input.setPlaceholderText("Enter text...");
        input.onAddCharLimited(System.out::println);
        input.onMouseMove((x, y) -> System.out.println("x=" + x + ", y=" + y));

        input.move(50);
        input.setBackgroundColor(VColor.white());
        setFocusedWidget(input);

        VLabel label = new VLabel("Hello world!", this).alsoAdd();

        label.onMouseDragLeft((oldX, oldY, newX, newY) -> setCursorShape(VCursorShape.VERTICAL_RESIZE));
        label.onLeftClickRelease(() -> setCursorShape(VCursorShape.DEFAULT));
        label.onMouseDragMiddle((oldX, oldY, newX, newY) -> setCursorShape(VCursorShape.ALL_RESIZE));
        label.onMiddleClickRelease(() -> setCursorShape(VCursorShape.DEFAULT));
        label.onMouseDragRight((oldX, oldY, newX, newY) -> setCursorShape(VCursorShape.HORIZONTAL_RESIZE));
        label.onRightClickRelease(() -> setCursorShape(VCursorShape.DEFAULT));
        label.onFilesDropped(System.out::println);

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

        VLabel centerLabel = new VLabel("CENTER", this).alsoAdd();
        centerLabel.setAlignment(VAlignmentFlag.CENTER);
        centerLabel.setBackgroundColor(VColor.black());
        centerLabel.modifyFontColor().rgb(255, 255, 255);
        centerLabel.move(220, 10);
        centerLabel.setBorder(VColor.MC_BLUE, VColor.MC_GOLD, VColor.MC_RED, VColor.MC_GREEN);
        centerLabel.setBorderSize(5, 10, 8, 16);
        centerLabel.setHoverCursor(VCursorShape.ALL_RESIZE);

        VLabel rightLabel = new VLabel("RIGHT", this).alsoAdd();
        rightLabel.setAlignment(VAlignmentFlag.RIGHT);
        rightLabel.setBackgroundColor(VColor.black());
        rightLabel.modifyFontColor().rgb(255, 255, 255);
        rightLabel.move(100, 10);
        rightLabel.setBorder(VColor.white());
        rightLabel.setBorderSize(1);
        rightLabel.onRightClick(() -> System.out.println(Vera.openFileSelector("test", Path.of("/Volumes/Media"), null)));

        VImage image = new VImage(
                Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png"),
                32, 32, this).alsoAdd();
        image.move(0, 30);
        image.onMiddleClick(this::hideCursor);
        image.onMiddleClickRelease(this::showCursor);

        VDropdown dropdown = new VDropdown(this).alsoAdd();
        dropdown.addItem("coolio");
        dropdown.addItem("shmoolio");
        dropdown.addItem("roolio", Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png"));
        dropdown.addItem("buger", () -> System.out.println("pressed"));
        dropdown.move(90);
        dropdown.setItemSpacing(16);
        dropdown.modifyHoverFont().color(VColor.white());
        dropdown.setItemHoverColor(VColor.black());
        dropdown.onFocusStateChange(() -> System.out.println("focus state change: " + dropdown.isFocused()));

        dropdown.getItem(2).setHoverIcon(Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/diamond_ore.png"));

        VCheckBox checkbox = new VCheckBox(this).alsoAdd();
        checkbox.move(20, 140);
        checkbox.setHoverOverlayColor(VColor.white().withOpacity(0.4f));

        checkbox.onCheckStateChange((state) -> {
            if (!state) removeWidget(checkbox);
        });

        VTabWidget tabs = new VTabWidget(this).alsoAdd();
        tabs.move(20, 170);
        tabs.addTab("test", checkbox);
        tabs.addTab("other test", dropdown);
        tabs.setActiveTab(0);

        VRect rotationRect = new VRect(VColor.black(), this).alsoAdd();
        rotationRect.rotate(45);
        rotationRect.move(20, 200);
    }

    @Override
    public void update() {
        super.update();

        move(0);
        setHeight(Vera.provider.getScreenHeight());
        setWidth(Vera.provider.getScreenWidth());
    }
}
