package net.snackbag.vera.style;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;

public class VStyleSheet {
    private final StyleContainer<VWidget<?>> widgetSpecificStyles = new StyleContainer<>(); // like HTML #IDs
    private final StyleContainer<String> classStyles = new StyleContainer<>(); // like CSS .classes
    private final StyleContainer<Class<?>> standardStyles = new StyleContainer<>(); // like HTML <tags/>

    private HashMap<String, StyleValueType> typeRegistry = new HashMap<>();

    public VStyleSheet() {
        this(true);
    }

    public VStyleSheet(boolean loadDefaultTypeRegistry) {
        if (loadDefaultTypeRegistry) {
            Vera.registrar.applyStandardWidgetStyles(this);
        }
    }

    public @Nullable <T> T getKey(VWidget<?> widget, String key) {
        return getKey(widget, key, VEffectState.DEFAULT);
    }

    public @Nullable <T> T getKey(VWidget<?> widget, String key, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        return getKey(widget, key, state.asStyleState());
    }

    public @Nullable <T> T getKey(VWidget<?> widget, String key, @NotNull VStyleState state) {
        // if widget contains key
        if (widgetSpecificStyles.hasKey(widget, key)) {
            // if widget has state, return
            if (widgetSpecificStyles.hasState(widget, key, state)) return widgetSpecificStyles.getState(widget, key, state);

            // if widget state has fallback, attempt
            if (state.hasFallback()) return getKey(widget, key, state.fallback());
        }

        // if class contains key
        HashMap<String, HashMap<VStyleState, Object>> mixed = mixClasses(widget.classes);

        Contains: if (mixed.containsKey(key)) {
            if (!mixed.get(key).containsKey(state)) {
                if (!state.hasFallback()) break Contains;
                return getKey(widget, key, state.fallback());
            }
            return (T) mixed.get(key).get(state);
        }

        // if nothing worked, try standard keys or return null
        return getStandardKey(widget.getClass(), key, state);
    }

    /**
     * In this case, stacked means that also all keys from states below the
     * given state are returned.
     */
    public Set<String> getKeysStacked(VWidget<?> widget, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        return getKeysStacked(widget, state.asStyleState());
    }

    /**
     * In this case, stacked means that also all keys from states below the
     * given state are returned.
     */
    public Set<String> getKeysStacked(VWidget<?> widget, @NotNull VStyleState state) {
        Set<String> keys = new HashSet<>();

        // standard styles
        keys.addAll(standardStyles.getKeysStacked(widget.getClass(), state));

        // class styles
        for (String clazz : widget.classes) {
            keys.addAll(classStyles.getKeysStacked(clazz, state));
        }

        // widget specific
        keys.addAll(widgetSpecificStyles.getKeysStacked(widget, state));

        return keys;
    }

    /**
     * Note: will return keys from states below the given state
     */
    public HashMap<String, Object> getKeyValuesStacked(VWidget<?> widget, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        return getKeyValuesStacked(widget, state.asStyleState());
    }

    /**
     * Note: will return keys from states below the given state
     */
    public HashMap<String, Object> getKeyValuesStacked(VWidget<?> widget, @NotNull VStyleState state) {
        Set<String> keys = getKeysStacked(widget, state);
        HashMap<String, Object> buffer = new HashMap<>();

        for (String key : keys) {
            buffer.put(key, getKey(widget, key, state));
        }

        return buffer;
    }

    /**
     * Resolves a standard style key by traversing the class hierarchy and style states.
     * <p>
     * Step by step:<br/>
     * 1. If <code>clazz</code> is <code>null</code>, return <code>null</code><br/>
     * 2. If <code>clazz</code> is not registered in {@link VStyleSheet#standardStyles}, recurse into its superclass<br/>
     * 3. If the specified <code>key</code> is not defined for this class, recurse into its superclass<br/>
     * 4. Check if the given <code>state</code> is defined:<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;- If not, and {@link VEffectState#fallback} exists, retry with the fallback state on the same class<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;- If fallback also fails or is <code>null</code>, recurse into the superclass with the original state
     */
    public <T> @Nullable T getStandardKey(@Nullable Class<?> clazz, String key, @NotNull VStyleState state) {
        if (clazz == null) return null;

        // if class isn't registered, attempt super
        if (!standardStyles.hasPart(clazz)) return getStandardKey(clazz.getSuperclass(), key, state);

        // if no key found, attempt super
        if (!standardStyles.hasKey(clazz, key)) return getStandardKey(clazz.getSuperclass(), key, state);

        // if no state found, return same class but fallback state
        if (!standardStyles.hasState(clazz, key, state)) {
            // if fallback state is null, attempt superclass
            if (!state.hasFallback()) return getStandardKey(clazz.getSuperclass(), key, state);

            // try fallback state
            return getStandardKey(clazz, key, state.fallback());
        }

        return standardStyles.getState(clazz, key, state);
    }

    public void setKey(VWidget<?> widget, String key, Object object) {
        setKey(widget, key, object, VEffectState.DEFAULT);
    }

    public void setKey(String clazz, String key, Object object) {
        setKey(clazz, key, object, VEffectState.DEFAULT);
    }

    public void setKey(Class<?> clazz, String key, Object object) {
        setKey(clazz, key, object, VEffectState.DEFAULT);
    }

    public void setKey(VWidget<?> widget, String key, Object value, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        setKey(widget, key, value, state.asStyleState());
    }

    public void setKey(VWidget<?> widget, String key, Object value, @NotNull VStyleState state) {
        setContainerKey(widgetSpecificStyles, widget, key, value, state);
    }

    public void setKey(String clazz, String key, Object value, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        setKey(clazz, key, value, state.asStyleState());
    }

    public void setKey(String clazz, String key, Object value, @NotNull VStyleState state) {
        setContainerKey(classStyles, clazz, key, value, state);
    }

    public void setKey(Class<?> clazz, String key, Object value, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        setKey(clazz, key, value, state.asStyleState());
    }

    public void setKey(Class<?> clazz, String key, Object value, @NotNull VStyleState state) {
        setContainerKey(standardStyles, clazz, key, value, state);
    }

    private <T> void setContainerKey(StyleContainer<T> container, T part, String key, Object value, @NotNull VStyleState state) {
        value = potentiallyUnpackArray(value);

        StyleValueType res = getReservation(key);
        StyleValueType valRes = StyleValueType.get(value, res);

        if (res != null) {
            if (valRes != res)
                throw new RuntimeException("Cannot set key %s (for %s), because it is reserved for type %s. Received: %s".formatted(key, part, res, valRes));
        } else reserveType(key, valRes);

        value = StyleValueType.convert(value, valRes);
        container.put(part, key, state, value);
    }

    public VColor.ColorModifier modifyKeyAsColor(VWidget<?> widget, String key) {
        return modifyKeyAsColor(widget, key, VEffectState.DEFAULT);
    }

    public VColor.ColorModifier modifyKeyAsColor(VWidget<?> widget, String key, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        return modifyKeyAsColor(widget, key, state.asStyleState());
    }

    public VColor.ColorModifier modifyKeyAsColor(VWidget<?> widget, String key, @NotNull VStyleState state) {
        @Nullable VStyleState finalState = state;
        return new VColor.ColorModifier(getKey(widget, key, state), color -> setKey(widget, key, color, finalState));
    }

    public VFont.FontModifier modifyKeyAsFont(VWidget<?> widget, String key) {
        return modifyKeyAsFont(widget, key, VEffectState.DEFAULT);
    }

    public VFont.FontModifier modifyKeyAsFont(VWidget<?> widget, String key, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        return modifyKeyAsFont(widget, key, state.asStyleState());
    }

    public VFont.FontModifier modifyKeyAsFont(VWidget<?> widget, String key, @NotNull VStyleState state) {
        @Nullable VStyleState finalState = state;
        return new VFont.FontModifier(getKey(widget, key, state), font -> setKey(widget, key, font, finalState));
    }

    public VColor.ColorModifier modifyKeyAsFontColor(VWidget<?> widget, String key) {
        return modifyKeyAsFontColor(widget, key, VEffectState.DEFAULT);
    }

    public VColor.ColorModifier modifyKeyAsFontColor(VWidget<?> widget, String key, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;
        return modifyKeyAsFontColor(widget, key, state.asStyleState());
    }

    public VColor.ColorModifier modifyKeyAsFontColor(VWidget<?> widget, String key, @NotNull VStyleState state) {
        // this is cursed
        @Nullable VStyleState finalState = state;
        return new VColor.ColorModifier(
                ((VFont) getKey(widget, key, state)).getColor(),
                color -> modifyKeyAsFont(widget, key, finalState).color(color)
        );
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
        standardStyles.moldWith(target.standardStyles);
        classStyles.moldWith(target.classStyles);
        widgetSpecificStyles.moldWith(target.widgetSpecificStyles);
    }

    public HashMap<String, HashMap<VStyleState, Object>> mixClasses(LinkedHashSet<String> classes) {
        final HashMap<String, HashMap<VStyleState, Object>> values = new HashMap<>();

        for (String clazz : classes) {
            HashMap<String, HashMap<VStyleState, Object>> styles = classStyles.getPart(clazz);
            for (String key : styles.keySet()) values.put(key, styles.get(key));
        }

        return values;
    }
}
