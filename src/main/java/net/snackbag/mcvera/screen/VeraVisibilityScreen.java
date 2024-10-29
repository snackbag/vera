package net.snackbag.mcvera.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;

/**
 * Existence is required for mouse movements
 */
public class VeraVisibilityScreen extends Screen {
    public VeraVisibilityScreen() {
        super(Text.of("Vera"));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (VeraApp app : MCVeraData.visibleApplications) {
            for (VWidget widget : app.getHoveredWidgets((int) mouseX, (int) mouseY)) {
                switch (button) {
                    case 0: widget.fireEvent("left-click");
                    case 1: widget.fireEvent("right-click");
                    case 2: widget.fireEvent("middle-click");
                    default: throw new IllegalStateException("Invalid button type: " + button);
                }
            }
        }

        return true;
    }
}
