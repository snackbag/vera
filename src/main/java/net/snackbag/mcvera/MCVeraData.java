package net.snackbag.mcvera;

import net.snackbag.vera.core.VeraApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MCVeraData {
    public static List<VeraApp> applications = new ArrayList<>();
    public static List<VeraApp> visibleApplications = new ArrayList<>();
    public final static List<Integer> pressedKeys = new ArrayList<>();
    public static final List<Integer> pressedKeys = new ArrayList<>();
    public static List<Integer> previousPressedKeys = new ArrayList<>();
    public static final Set<VeraApp> debugApps = new HashSet<>();
}
