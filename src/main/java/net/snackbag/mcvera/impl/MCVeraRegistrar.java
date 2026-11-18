package net.snackbag.mcvera.impl;

import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.style.standard.VStandardStyle;
import net.snackbag.vera.style.VStyleSheet;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Main Vera registry manager. Use with caution: there are (almost) no
 * safety checks. Therefore, it is recommended to use the classes that directly
 * implement registrar functionality than touching it yourself.
 */
public class MCVeraRegistrar {
    private final Set<VStandardStyle> standardStyles = new LinkedHashSet<>();
    private final HashMap<String, VEasing> easings = new HashMap<>();

    public void registerStandardStyle(VStandardStyle style) {
        standardStyles.add(style);
    }

    public void applyStandardWidgetStyles(VStyleSheet sheet) {
        for (VStandardStyle standardStyle : standardStyles) {
            standardStyle.reserve(sheet);
            standardStyle.apply(sheet);
        }
    }

    public void registerEasing(String name, VEasing easing) {
        easings.put(name.toLowerCase(), easing);
    }

    public @Nullable VEasing getEasingIgnoreCase(String name) {
        return easings.getOrDefault(name.toLowerCase(), null);
    }
}
