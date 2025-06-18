package net.snackbag.vera.style;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class VStyleSheet {
    private final HashMap<VWidget<?>, HashMap<String, HashMap<StyleState, Object>>> widgetSpecificStyles = new HashMap<>();
    private final HashMap<String, HashMap<String, HashMap<StyleState, Object>>> styleClasses = new HashMap<>();
    private final HashMap<String, StyleValueType> typeRegistry = new HashMap<>();

    public VStyleSheet() {
        // Generic
        reserveType("overlay", StyleValueType.COLOR);
        reserveType("background-color", StyleValueType.COLOR);
        reserveType("src", StyleValueType.IDENTIFIER);
        reserveType("cursor", StyleValueType.CURSOR);

        // Checkbox
        reserveType("src-checked", StyleValueType.IDENTIFIER);

        // Line Input
        reserveType("select-color", StyleValueType.COLOR);

        // Font
        reserveType("color", StyleValueType.COLOR);
        reserveType("font", StyleValueType.FONT);
        reserveType("font-size", StyleValueType.INT);
        reserveType("font-name", StyleValueType.STRING);
    }

    public <T> T getKey(VWidget<?> widget, String key) {
        return getKey(widget, key, StyleState.DEFAULT);
    }

    public <T> T getKey(VWidget<?> widget, String key, @Nullable StyleState state) {
        if (state == null) state = StyleState.DEFAULT;

        if (widgetSpecificStyles.containsKey(widget) && widgetSpecificStyles.get(widget).containsKey(key)) {
            if (!widgetSpecificStyles.get(widget).get(key).containsKey(state)) return getKey(widget, key, state.fallback);
            return (T) widgetSpecificStyles.get(widget).get(key).get(state);
        }

        HashMap<String, HashMap<StyleState, Object>> mixed = mixClasses(widget.classes);

        if (mixed.containsKey(key)) {
            if (!mixed.get(key).containsKey(state)) return getKey(widget, key, state.fallback);
            return (T) mixed.get(key).get(state);
        }

        return null;
    }

    public void setKey(VWidget<?> widget, String key, Object object) {
        setKey(widget, key, object, StyleState.DEFAULT);
    }

    public void setKey(VWidget<?> widget, String key, Object value, @Nullable StyleState state) {
        if (state == null) state = StyleState.DEFAULT;

        StyleValueType res = getReservation(key);
        StyleValueType valRes = StyleValueType.get(value, res);

        if (res != null) {
            if (valRes != res)
                throw new RuntimeException("Cannot set key %s, because it is reserved for type %s. Received: %s".formatted(key, res, valRes));
        } else reserveType(key, valRes);

        value = StyleValueType.convert(value, valRes);

        if (!widgetSpecificStyles.containsKey(widget)) widgetSpecificStyles.put(widget, new HashMap<>());
        if (!widgetSpecificStyles.get(widget).containsKey(key)) widgetSpecificStyles.get(widget).put(key, new HashMap<>());
        widgetSpecificStyles.get(widget).get(key).put(state, value);
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

    public HashMap<String, HashMap<StyleState, Object>> getClassStyles(String clazz) {
        return styleClasses.getOrDefault(clazz, new HashMap<>());
    }

    public HashMap<String, HashMap<StyleState, Object>> mixClasses(LinkedHashSet<String> classes) {
        final HashMap<String, HashMap<StyleState, Object>> values = new HashMap<>();

        for (String clazz : classes) {
            HashMap<String, HashMap<StyleState, Object>> styles = getClassStyles(clazz);
            for (String key : styles.keySet()) values.put(key, styles.get(key));
        }

        return values;
    }
}
