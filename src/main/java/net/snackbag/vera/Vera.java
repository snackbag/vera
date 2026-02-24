package net.snackbag.vera;

import net.minecraft.client.MinecraftClient;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRegistrar;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VAppFlag;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Vera {
    public static final MCVeraProvider provider = new MCVeraProvider();
    public static final MCVeraRenderer renderer = new MCVeraRenderer();
    public static final MCVeraRegistrar registrar = new MCVeraRegistrar();

    public static final String FONT_DEFAULT = provider.getDefaultFontName();
    public static final String FONT_ARIAL = "minecraft:arial";

    public static long renderCacheId = 0;
    protected static final ArrayList<Runnable> nextFrameTasks = new ArrayList<>();

    public static void forVisibleAndAllowedApps(Consumer<VeraApp> handler) {
        final List<VeraApp> handledApps = new ArrayList<>();

        VeraApp topHierarchy = MCVeraData.getTopHierarchy();
        if (topHierarchy != null) {
            handledApps.add(topHierarchy);
            handler.accept(topHierarchy);
        }

        for (VWindowPositioningFlag flag : MCVeraData.visibleApplications.keySet()) {
            for (VeraApp app : MCVeraData.visibleApplications.get(flag)) {
                if (handledApps.contains(app) || app.hasFlag(VAppFlag.HIERARCHIC)) continue;

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

    @SafeVarargs
    public static <T> @Nullable T firstOf(Predicate<? super T> evaluator, T... values) {
        for (T v : values) {
            if (v == null) continue;
            if (evaluator.test(v)) return v;
        }

        return null;
    }

    /**
     * Schedules a task to the next frame; run AFTER all Vera rendering
     * @param runnable the task to execute
     */
    public static void scheduleToNextFrame(Runnable runnable) {
        nextFrameTasks.add(runnable);
    }
}
