package net.snackbag.mcvera.test;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.flag.VWindowFlag;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.widget.VLabel;
import net.snackbag.vera.widget.VRect;

public class HierarchyTest {
    public static HierarchyTest INSTANCE = new HierarchyTest();

    public void start() {
        Application first = new Application("first");
        Application second = new Application("second");
        Application third = new Application("third");

        first.show();
        first.move(10);

        second.show();
        second.move(120, 10);

        third.show();
        third.move(230, 10);

        Vera.scheduleToNextFrame(first::moveToHierarchyTop);
    }

    public static class Application extends VeraApp {
        private final String name;

        public Application(String name) {
            this.name = name;
        }

        @Override
        public void init() {
            new VShortcut(this, "escape", this::hide);

            setBackgroundColor(VColor.MC_DARK_GRAY);
            setFlag(VWindowFlag.HIERARCHIC, true);

            styleSheet.setKey(VLabel.class, "font", VFont.create().withColor(VColor.white()).withSize(12));
            styleSheet.setKey(VLabel.class, "font", VFont.create().withColor(VColor.MC_AQUA).withSize(12), StyleState.HOVERED);
            styleSheet.setKey(VLabel.class, "transition", 250);

            VRect mover = new VRect(VColor.black(), 0, 0, 100, 8, this).alsoAdd();
            mover.onMouseDragLeft((ctx) -> move(getX() + ctx.moveX(), getY() + ctx.moveY()));

            VLabel label = new VLabel(name, this).alsoAdd();
            label.move(1);
        }

        @Override
        public void update() {
            setSize(100, 200);
        }
    }
}
