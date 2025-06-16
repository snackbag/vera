package net.snackbag.mcvera;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VWindowPositioningFlag;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class MCVeraData {
    public static LinkedHashSet<VeraApp> applications = new LinkedHashSet<>();
    public static HashMap<VWindowPositioningFlag, LinkedHashSet<VeraApp>> visibleApplications = new HashMap<>();
    public static List<VeraApp> appHierarchy = new ArrayList<>();

    public static int appsWithMouseRequired = 0;
    public static final Set<VeraApp> debugApps = new HashSet<>();

    public static final List<Integer> pressedKeys = new ArrayList<>();
    public static List<Integer> previousPressedKeys = new ArrayList<>();

    /**
     * Executes a method as the top hierarchy app. If there is no top app it won't execute the specified code.
     *
     * @param runnable code to execute
     * @return whether something has been executed
     */
    public static boolean asTopHierarchy(@NotNull Consumer<VeraApp> runnable) {
        if (appHierarchy.isEmpty()) return false;

        runnable.accept(appHierarchy.get(0));
        return true;
    }
}
