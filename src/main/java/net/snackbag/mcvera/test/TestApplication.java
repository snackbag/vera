package net.snackbag.mcvera.test;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.flag.VHAlignmentFlag;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.widget.*;

import java.nio.file.Path;

public class TestApplication extends VeraApp {
    public static TestApplication INSTANCE = new TestApplication();

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

        VLineInput input = new VLineInput(this).alsoAdd();
        input.setMaxChars(15);
        input.setPlaceholderText("Enter text...");
        input.onAddCharLimited(System.out::println);
        input.onMouseMove((x, y) -> System.out.println("x=" + x + ", y=" + y));

        input.move(50);
        input.setStyle("background-color", VColor.white());
        setFocusedWidget(input);

        VLabel label = new VLabel("Hello world!", this).alsoAdd();

        label.onMouseDragLeft((ctx) -> setCursorShape(VCursorShape.VERTICAL_RESIZE));
        label.onLeftClickRelease(() -> setCursorShape(VCursorShape.DEFAULT));
        label.onMouseDragMiddle((ctx) -> setCursorShape(VCursorShape.ALL_RESIZE));
        label.onMiddleClickRelease(() -> setCursorShape(VCursorShape.DEFAULT));
        label.onMouseDragRight((ctx) -> setCursorShape(VCursorShape.HORIZONTAL_RESIZE));
        label.onRightClickRelease(() -> setCursorShape(VCursorShape.DEFAULT));
        label.onFilesDropped(System.out::println);

        label.setStyle("padding", 5);
        label.move(10);
        label.setStyle("background-color", VColor.black());
        label.modifyFont("font").color(VColor.white());
        label.adjustSize();
        label.onHover(() -> {
            label.setText("Hovered");
        });

        label.onHoverLeave(() -> {
            label.setText("Not hovered");
        });

        VLabel centerLabel = new VLabel("CENTER", 220, 10, 100, 16, this).alsoAdd();
        centerLabel.setAlignment(VHAlignmentFlag.CENTER);
        centerLabel.setStyle("background-color", VColor.black());
        centerLabel.modifyFontColor("font").rgb(255, 255, 255);
        centerLabel.setStyle("border-color", VColor.MC_BLUE, VColor.MC_GOLD, VColor.MC_RED, VColor.MC_GREEN);
        centerLabel.setStyle("border-size", 5, 10, 8, 16);
        centerLabel.setStyle("cursor", VCursorShape.ALL_RESIZE);

        VLabel rightLabel = new VLabel("RIGHT", 100, 10, 100, 16, this).alsoAdd();
        rightLabel.setAlignment(VHAlignmentFlag.RIGHT);
        rightLabel.setStyle("background-color", VColor.black());
        rightLabel.modifyFontColor("font").rgb(255, 255, 255);
        rightLabel.setStyle("border-color", VColor.white());
        rightLabel.setStyle("border-size", 1);
        rightLabel.onRightClick(() -> System.out.println(Vera.openFileSelector("test", Path.of("/Volumes/Media"), null)));

        VImage image = new VImage(
                "minecraft:textures/block/dirt.png",
                32, 32, this).alsoAdd();
        image.move(0, 30);
        image.onMiddleClick(this::hideCursor);
        image.onMiddleClickRelease(this::showCursor);
        image.setStyle("src", StyleState.HOVERED, "minecraft:textures/block/diamond_block.png");

        VDropdown dropdown = new VDropdown(this).alsoAdd();
        dropdown.addItem("coolio");
        dropdown.addItem("shmoolio");
        dropdown.addItem("roolio", Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/dirt.png"));
        dropdown.addItem("buger", () -> System.out.println("pressed"));
        dropdown.move(90);
        dropdown.setItemSpacing(16);
        dropdown.itemHoverFont = VFont.create().withColor(VColor.white());
        dropdown.setItemHoverColor(VColor.black());
        dropdown.onFocusStateChange(() -> System.out.println("focus state change: " + dropdown.isFocused()));

        dropdown.getItem(2).setHoverIcon(Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/block/diamond_ore.png"));

        VCheckBox checkbox = new VCheckBox(this).alsoAdd();
        checkbox.move(20, 140);
        checkbox.setStyle("overlay", StyleState.HOVERED, VColor.white().withOpacity(0.4f));

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
