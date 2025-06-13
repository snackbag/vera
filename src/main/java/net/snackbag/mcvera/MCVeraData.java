package net.snackbag.mcvera;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.flag.VWindowPositioningFlag;

import java.util.*;

public class MCVeraData {
    public static LinkedHashSet<VeraApp> applications = new LinkedHashSet<>();
    public static HashMap<VWindowPositioningFlag, LinkedHashSet<VeraApp>> visibleApplications = new HashMap<>();

    public static int appsWithMouseRequired = 0;
    public static final Set<VeraApp> debugApps = new HashSet<>();

    public static final List<Integer> pressedKeys = new ArrayList<>();
    public static List<Integer> previousPressedKeys = new ArrayList<>();
}
