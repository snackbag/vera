package net.snackbag.vera.style;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class VStyleSheet {
    private final HashMap<VWidget<?>, HashMap<String, Object>> widgetSpecificStyles = new HashMap<>();
    private final HashMap<String, HashMap<String, Object>> styleClasses = new HashMap<>();
    private final HashMap<String, StyleValueType> typeRegistry = new HashMap<>();

    public VStyleSheet() {
        // Generic
        reserveType("background-color", StyleValueType.COLOR);
        reserveType("src", StyleValueType.IDENTIFIER);

        // Font
        reserveType("color", StyleValueType.COLOR);
        reserveType("font", StyleValueType.FONT);
        reserveType("font-size", StyleValueType.INT);
        reserveType("font-name", StyleValueType.STRING);
    }

    public <T> T getKey(VWidget<?> widget, String key) {
        if (widgetSpecificStyles.containsKey(widget) && widgetSpecificStyles.get(widget).containsKey(key)) {
            return (T) widgetSpecificStyles.get(widget).get(key);
        }

        HashMap<String, Object> mixed = mixClasses(widget.classes);

        if (mixed.containsKey(key)) {
            return (T) mixed.get(key);
        }

        return null;
    }

    public void setKey(VWidget<?> widget, String key, Object value) {
        StyleValueType res = getReservation(key);
        StyleValueType valRes = StyleValueType.get(value);

        if (res != null) {
            if (valRes != res)
                throw new RuntimeException("Cannot set key %s, because it is reserved for type %s. Received: %s".formatted(key, res, valRes));
        } else reserveType(key, valRes);

        if (!widgetSpecificStyles.containsKey(widget)) widgetSpecificStyles.put(widget, new HashMap<>());
        widgetSpecificStyles.get(widget).put(key, value);
    }

    public VColor.ColorModifier modifyKeyAsColor(VWidget<?> widget, String key) {
        return new VColor.ColorModifier(getKey(widget, key), color -> setKey(widget, key, color));
    }

    public VFont.FontModifier modifyKeyAsFont(VWidget<?> widget, String key) {
        return new VFont.FontModifier(getKey(widget, key), font -> setKey(widget, key, font));
    }

    public void reserveType(String key, StyleValueType type) {
        typeRegistry.put(key, type);
    }

    public @Nullable StyleValueType getReservation(String key) {
        return typeRegistry.getOrDefault(key, null);
    }

    public HashMap<String, Object> getClassStyles(String clazz) {
        return styleClasses.getOrDefault(clazz, new HashMap<>());
    }

    public HashMap<String, Object> mixClasses(LinkedHashSet<String> classes) {
        final HashMap<String, Object> values = new HashMap<>();

        for (String clazz : classes) {
            HashMap<String, Object> styles = getClassStyles(clazz);
            for (String key : styles.keySet()) values.put(key, styles.get(key));
        }

        return values;
    }
}
