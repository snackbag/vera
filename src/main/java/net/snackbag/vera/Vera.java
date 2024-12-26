package net.snackbag.vera;

import net.minecraft.client.MinecraftClient;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class Vera {
    public static final MCVeraProvider provider = new MCVeraProvider();
    public static final MCVeraRenderer renderer = new MCVeraRenderer();

    public static final String FONT_DEFAULT = provider.getDefaultFontName();
    public static final String FONT_ARIAL = "minecraft:arial";

    public static void forHoveredWidget(int mouseX, int mouseY, Consumer<VWidget<?>> runnable, @Nullable Consumer<VeraApp> ifEmpty) {
        for (VeraApp app : MCVeraData.visibleApplications) {
            List<VWidget<?>> hoveredWidgets = app.getHoveredWidgets(mouseX, mouseY);
            if (hoveredWidgets.isEmpty()) {
                if (ifEmpty != null) ifEmpty.accept(app);
                return;
            }

            for (VWidget<?> widget : hoveredWidgets) {
                runnable.accept(widget);
            }
        }
    }

    public static void forHoveredWidget(int mouseX, int mouseY, Consumer<VWidget<?>> runnable) {
        forHoveredWidget(mouseX, mouseY, runnable, null);
    }

    public static void forHoveredWidgetIfEmpty(int mouseX, int mouseY, Consumer<VeraApp> runnable) {
        for (VeraApp app : MCVeraData.visibleApplications) {
            List<VWidget<?>> hoveredWidgets = app.getHoveredWidgets(mouseX, mouseY);
            if (hoveredWidgets.isEmpty()) runnable.accept(app);
        }
    }

    public static int getMouseX() {
        return (int) (MinecraftClient.getInstance().mouse.getX() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    public static int getMouseY() {
        return (int) (MinecraftClient.getInstance().mouse.getY() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }
}
