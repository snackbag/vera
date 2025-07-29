package net.snackbag.vera;

import net.minecraft.client.MinecraftClient;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRegistrar;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Vera {
    public static final MCVeraProvider provider = new MCVeraProvider();
    public static final MCVeraRenderer renderer = new MCVeraRenderer();
    public static final MCVeraRegistrar registrar = new MCVeraRegistrar();

    public static final String FONT_DEFAULT = provider.getDefaultFontName();
    public static final String FONT_ARIAL = "minecraft:arial";

    public static long renderCacheId = 0;

    public static void forVisibleAndAllowedApps(Consumer<VeraApp> handler) {
        final List<VeraApp> handledApps = new ArrayList<>();
        if (!MCVeraData.appHierarchy.isEmpty()) {
            VeraApp app = MCVeraData.appHierarchy.get(0);

            handledApps.add(app);
            handler.accept(app);
        }

        for (VWindowPositioningFlag flag : MCVeraData.visibleApplications.keySet()) {
            for (VeraApp app : MCVeraData.visibleApplications.get(flag)) {
                if (handledApps.contains(app) || app.isRequiresHierarchy()) continue;

                handler.accept(app);
                handledApps.add(app);
            }
        }
    }

    public static void forAllVisibleApps(Consumer<VeraApp> handler) {
        for (VWindowPositioningFlag flag : MCVeraData.visibleApplications.keySet()) {
            for (VeraApp app : MCVeraData.visibleApplications.get(flag)) {
                handler.accept(app);
            }
        }
    }

    public static int getMouseX() {
        return (int) (MinecraftClient.getInstance().mouse.getX() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    public static int getMouseY() {
        return (int) (MinecraftClient.getInstance().mouse.getY() / MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    public static @Nullable String openFileSelector(@Nullable String title, Path defaultPath, @Nullable String filter) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer filters;
            if (filter == null) {
                filters = null;
            } else {
                filters = stack.mallocPointer(1);
                filters.put(stack.UTF8(filter).flip());
            }

            @Nullable
            String path = TinyFileDialogs.tinyfd_openFileDialog(
                    title,
                    defaultPath.toAbsolutePath().toString(),
                    filters,
                    null,
                    false
            );
            return path;
        }
    }

    public static @Nullable VeraApp getTopHierarchyApp() {
        return MCVeraData.appHierarchy.isEmpty() ? null : MCVeraData.appHierarchy.get(0);
    }

    public static boolean isTopHierarchy(VeraApp app) {
        return getTopHierarchyApp() == app;
    }
}
