package net.snackbag.mcvera;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VAppFlag;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class MCVeraData {
    public static LinkedHashSet<VeraApp> applications = new LinkedHashSet<>();
    public static HashMap<VWindowPositioningFlag, LinkedHashSet<VeraApp>> visibleApplications = new HashMap<>();
    public static HashMap<VAppFlag, List<VeraApp>> appFlags = new HashMap<>();

    public static int appsWithMouseRequired = 0;

    public static final List<Integer> pressedKeys = new ArrayList<>();
    public static List<Integer> previousPressedKeys = new ArrayList<>();

    /**
     * Executes a method as the top hierarchy app. If there is no top app it won't execute the specified code.
     *
     * @param runnable code to execute
     * @return whether something has been executed
     */
    public static boolean asTopHierarchy(@NotNull Consumer<VeraApp> runnable) {
        if (!appFlags.containsKey(VAppFlag.HIERARCHIC)) return false;
        runnable.accept(getTopHierarchy());
        return true;
    }

    public static @Nullable VeraApp getTopHierarchy() {
        List<VeraApp> apps = getAppsWithFlag(VAppFlag.HIERARCHIC);
        if (apps.isEmpty()) return null;
        return apps.get(0);
    }

    public static boolean isTopHierarchy(VeraApp app) {
        return getTopHierarchy() == app;
    }

    /**
     * Returns an UNMODIFIABLE version of the {@link #appFlags} entry for the given flag. If the entry is empty, it
     * returns an empty unmodifiable list. If you want to access a modifiable version of the flag, you have to manually
     * work with the {@link #appFlags} variable.
     *
     * @param flag the flag to check
     * @return an unmodifiable list of the apps under the flag
     */
    public static List<VeraApp> getAppsWithFlag(VAppFlag flag) {
        if (!appFlags.containsKey(flag)) return Collections.unmodifiableList(new ArrayList<>());
        return Collections.unmodifiableList(appFlags.get(flag));
    }
}
