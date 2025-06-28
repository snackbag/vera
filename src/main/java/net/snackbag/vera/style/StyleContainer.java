package net.snackbag.vera.style;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Holds types, keys and states.
 * <p>
 * Consists of:<br/>
 * | Part<br/>
 * |--- Key<br/>
 * |------ StyleState:Object
 */
public class StyleContainer<T> {
    private final HashMap<T, HashMap<String, HashMap<StyleState, Object>>> values = new HashMap<>();

    public StyleContainer() {}

    public boolean hasPart(T part) {
        return values.containsKey(part);
    }

    public HashMap<String, HashMap<StyleState, Object>> getPart(T part) {
        return values.getOrDefault(part, new HashMap<>());
    }

    public boolean hasKey(T part, String key) {
        return getPart(part).containsKey(key);
    }

    public HashMap<StyleState, Object> getKey(T part, String key) {
        return getPart(part).getOrDefault(key, new HashMap<>());
    }

    public boolean hasState(T part, String key, StyleState state) {
        return getKey(part, key).containsKey(state);
    }

    public <V> V getState(T part, String key, StyleState state) {
        return (V) getKey(part, key).get(state);
    }

    public void put(T part, String key, StyleState state, Object value) {
        if (!hasPart(part)) values.put(part, new HashMap<>());
        if (!hasKey(part, key)) values.get(part).put(key, new HashMap<>());
        if (!hasState(part, key, state)) values.get(part).get(key).put(state, new HashMap<>());

        values.get(part).get(key).put(state, value);
    }

    public void moldWith(StyleContainer<T> target) {
        for (T targetPart : target.values.keySet()) {
            if (!hasPart(targetPart)) {
                values.put(targetPart, target.getPart(targetPart));
                continue;
            }

            for (String targetKey : target.getPart(targetPart).keySet()) {
                if (!hasKey(targetPart, targetKey)) {
                    values.get(targetPart).put(targetKey, target.getKey(targetPart, targetKey));
                    continue;
                }

                for (StyleState targetState : target.getKey(targetPart, targetKey).keySet()) {
                    if (!hasState(targetPart, targetKey, targetState)) {
                        values.get(targetPart).get(targetKey).put(targetState, target.getState(targetPart, targetKey, targetState));
                        continue;
                    }
                }
            }
        }
    }
}
