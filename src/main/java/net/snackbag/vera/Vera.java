package net.snackbag.vera;

import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;

import java.util.List;
import java.util.function.Consumer;

public class Vera {
    public static final MCVeraProvider provider = new MCVeraProvider();
    public static final MCVeraRenderer renderer = new MCVeraRenderer();

    public static final String FONT_DEFAULT = provider.getDefaultFontName();
    public static final String FONT_ARIAL = "minecraft:arial";

    public static void forHoveredWidget(int mouseX, int mouseY, Consumer<VWidget<?>> runnable) {
        for (VeraApp app : MCVeraData.visibleApplications) {
            List<VWidget<?>> hoveredWidgets = app.getHoveredWidgets(mouseX, mouseY);
            if (hoveredWidgets.isEmpty()) return;

            for (VWidget<?> widget : hoveredWidgets) {
                runnable.accept(widget);
            }
        }
    }
}
