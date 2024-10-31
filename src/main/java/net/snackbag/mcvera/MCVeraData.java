package net.snackbag.mcvera;

import net.snackbag.vera.core.VeraApp;

import java.util.ArrayList;
import java.util.List;

public class MCVeraData {
    public static List<VeraApp> applications = new ArrayList<>();
    public static List<VeraApp> visibleApplications = new ArrayList<>();
    public final static List<Integer> pressedKeys = new ArrayList<>();
    public static List<Integer> previousPressedKeys = new ArrayList<>();
}
