package net.snackbag.mcvera.impl;

import net.snackbag.vera.style.standard.VStandardStyle;
import net.snackbag.vera.style.VStyleSheet;

import java.util.ArrayList;
import java.util.List;

public class MCVeraRegistrar {
    private final List<VStandardStyle> standardStyles = new ArrayList<>();

    public void registerStandardStyle(VStandardStyle style) {
        standardStyles.add(style);
    }

    public void applyStandardWidgetStyles(VStyleSheet sheet) {
        for (VStandardStyle standardStyle : standardStyles) {
            standardStyle.apply(sheet);
        }
    }
}
