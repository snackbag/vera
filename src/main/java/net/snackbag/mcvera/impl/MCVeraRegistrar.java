package net.snackbag.mcvera.impl;

import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.style.standard.VStandardStyle;
import net.snackbag.vera.style.VStyleSheet;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MCVeraRegistrar {
    private final List<VStandardStyle> standardStyles = new ArrayList<>();
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
