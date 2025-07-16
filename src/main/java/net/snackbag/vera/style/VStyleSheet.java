package net.snackbag.vera.style;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class VStyleSheet {
    private final StyleContainer<VWidget<?>> widgetSpecificStyles = new StyleContainer<>(); // like HTML #IDs
    private final StyleContainer<String> classStyles = new StyleContainer<>(); // like CSS .classes
    private final StyleContainer<Class<?>> standardStyles = new StyleContainer<>(); // like HTML <tags/>

    private HashMap<String, StyleValueType> typeRegistry = new HashMap<>();

    public VStyleSheet() {
        // Generic
        reserveType("color", StyleValueType.COLOR);
        reserveType("src", StyleValueType.IDENTIFIER);

        // Checkbox
        reserveType("src-checked", StyleValueType.IDENTIFIER);

        // Line Input
        reserveType("select-color", StyleValueType.COLOR);
        reserveType("color-cursor", StyleValueType.COLOR);
    }

    public <T> T getKey(VWidget<?> widget, String key) {
        return getKey(widget, key, StyleState.DEFAULT);
    }

    public <T> T getKey(VWidget<?> widget, String key, @Nullable StyleState state) {
        if (state == null) state = StyleState.DEFAULT;

        HashMap<String, HashMap<StyleState, Object>> mixed = mixClasses(widget.classes);

        if (mixed.containsKey(key)) {
            if (!mixed.get(key).containsKey(state)) return getKey(widget, key, state.fallback);
            return (T) mixed.get(key).get(state);
        }

        // if widget contains key
        if (widgetSpecificStyles.hasKey(widget, key)) {
            // if it doesn't contain the state return with the fallback
            if (!widgetSpecificStyles.hasState(widget, key, state)) return getKey(widget, key, state.fallback);
            return widgetSpecificStyles.getState(widget, key, state);
        }

        // if nothing worked, return null
        return null;
    }

    public void setKey(VWidget<?> widget, String key, Object object) {
        setKey(widget, key, object, StyleState.DEFAULT);
    }

    public void setKey(String clazz, String key, Object object) {
        setKey(clazz, key, object, StyleState.DEFAULT);
    }

    public void setKey(Class<?> clazz, String key, Object object) {
        setKey(clazz, key, object, StyleState.DEFAULT);
    }

    public void setKey(VWidget<?> widget, String key, Object value, @Nullable StyleState state) {
        if (state == null) state = StyleState.DEFAULT;
        value = potentiallyUnpackArray(value);

        StyleValueType res = getReservation(key);
        StyleValueType valRes = StyleValueType.get(value, res);

        if (res != null) {
            if (valRes != res)
                throw new RuntimeException("Cannot set key %s, because it is reserved for type %s. Received: %s".formatted(key, res, valRes));
        } else reserveType(key, valRes);

        value = StyleValueType.convert(value, valRes);
        widgetSpecificStyles.put(widget, key, state, value);
    }

    public void setKey(String clazz, String key, Object value, @Nullable StyleState state) {
        if (state == null) state = StyleState.DEFAULT;
        value = potentiallyUnpackArray(value);

        StyleValueType res = getReservation(key);
        StyleValueType valRes = StyleValueType.get(value, res);

        if (res != null) {
            if (valRes != res)
                throw new RuntimeException("Cannot set key %s (for class %s), because it is reserved for type %s. Received: %s".formatted(key, clazz, res, valRes));
        } else reserveType(key, valRes);

        value = StyleValueType.convert(value, valRes);
        classStyles.put(clazz, key, state, value);
    }

    public void setKey(Class<?> clazz, String key, Object value, @Nullable StyleState state) {
        if (state == null) state = StyleState.DEFAULT;
        value = potentiallyUnpackArray(value);

        StyleValueType res = getReservation(key);
        StyleValueType valRes = StyleValueType.get(value, res);

        if (res != null) {
            if (valRes != res)
                throw new RuntimeException("Cannot set standard key %s (for class %s), because it is reserved for type %s. Received: %s".formatted(key, clazz, res, valRes));
        } else reserveType(key, valRes);

        value = StyleValueType.convert(value, valRes);
        standardStyles.put(clazz, key, state, value);
    }

    public VColor.ColorModifier modifyKeyAsColor(VWidget<?> widget, String key) {
        return new VColor.ColorModifier(getKey(widget, key), color -> setKey(widget, key, color));
    }

    public VFont.FontModifier modifyKeyAsFont(VWidget<?> widget, String key) {
        return new VFont.FontModifier(getKey(widget, key), font -> setKey(widget, key, font));
    }

    public void reserveType(String key, StyleValueType type) { // TODO: make safer (aka stop if already reserved or values inside)
        typeRegistry.put(key, type);
    }

    public @Nullable StyleValueType getReservation(String key) {
        return typeRegistry.getOrDefault(key, null);
    }

    private Object potentiallyUnpackArray(Object value) {
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            if (length == 1) value = Array.get(value, 0);
        }

        return value;
    }

    public void addSheet(VStyleSheet target) {
        // Merge type registries
        HashMap<String, StyleValueType> mergedTypeRegistry = new HashMap<>(typeRegistry);

        for (String key : target.typeRegistry.keySet()) {
            StyleValueType type = target.typeRegistry.get(key);

            if (!mergedTypeRegistry.containsKey(key)) mergedTypeRegistry.put(key, type);
            else if (mergedTypeRegistry.get(key) != type)
                throw new UnsupportedOperationException("Cannot merge two sheets with different type registry entries. Received %s:%s; already %s".formatted(key, type, mergedTypeRegistry.get(key)));
        }

        // Apply changes if everything went fine
        typeRegistry = mergedTypeRegistry;
        classStyles.moldWith(target.classStyles);
        widgetSpecificStyles.moldWith(target.widgetSpecificStyles);
    }

    public HashMap<String, HashMap<StyleState, Object>> mixClasses(LinkedHashSet<String> classes) {
        final HashMap<String, HashMap<StyleState, Object>> values = new HashMap<>();

        for (String clazz : classes) {
            HashMap<String, HashMap<StyleState, Object>> styles = classStyles.getPart(clazz);
            for (String key : styles.keySet()) values.put(key, styles.get(key));
        }

        return values;
    }
}
