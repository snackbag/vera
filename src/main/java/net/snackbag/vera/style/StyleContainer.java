package net.snackbag.vera.style;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds types, keys and states.
 * <p>
 * Consists of:<br/>
 * | Part<br/>
 * |--- Key<br/>
 * |------ VEffectState:Object
 */
public class StyleContainer<T> {
    private final HashMap<T, HashMap<String, HashMap<VEffectState, Object>>> values = new HashMap<>();

    public StyleContainer() {}

    public boolean hasPart(T part) {
        return values.containsKey(part);
    }

    public HashMap<String, HashMap<VEffectState, Object>> getPart(T part) {
        return values.getOrDefault(part, new HashMap<>());
    }

    public boolean hasKey(T part, String key) {
        return getPart(part).containsKey(key);
    }

    public HashMap<VEffectState, Object> getKey(T part, String key) {
        return getPart(part).getOrDefault(key, new HashMap<>());
    }

    public boolean hasState(T part, String key, VEffectState state) {
        return getKey(part, key).containsKey(state);
    }

    public <V> V getState(T part, String key, VEffectState state) {
        return (V) getKey(part, key).get(state);
    }


    /**
     * In this case, exact means that it does not resolve lower states and only
     * gives the keys of exactly the given style state. Use {@link #getKeysStacked(Object, VEffectState)}
     * for deeper state resolve.
     * <br/><br/>
     * For example when requesting state <code>HOVERED</code>:
     * <table>
     *     <tr>
     *         <th>Key</th>
     *         <th>State</th>
     *         <th>Returned</th>
     *     </tr>
     *     <tr>
     *         <td>src</th>
     *         <td>DEFAULT</th>
     *         <td>No</th>
     *     </tr>
     *     <tr>
     *         <td>overlay</th>
     *         <td>HOVERED</th>
     *         <td>Yes</th>
     *     </tr>
     *     <tr>
     *         <td>font</td>
     *         <td>CLICKED</td>
     *         <td>No</td>
     *     </tr>
     * </table>
     *
     * @see #getKeysStacked(Object, VEffectState)
     */
    public Set<String> getKeysExact(T part, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;

        Set<String> buffer = new HashSet<>();

        var resolvedPart = getPart(part); // i'm sorry for using var but holy fuck
        for (String key : resolvedPart.keySet()) {
            for (VEffectState keyState : resolvedPart.get(key).keySet()) {
                if (keyState != state) continue;
                buffer.add(key);
            }
        }

        return buffer;
    }

    /**
     * In this case, stacked means that also all keys from states below the
     * given state are returned. Use {@link #getKeysExact(Object, VEffectState)} for
     * only the exact keys of a style state.
     * <br/><br/>
     * For example when requesting state <code>HOVERED</code>:
     * <table>
     *     <tr>
     *         <th>Key</th>
     *         <th>State</th>
     *         <th>Returned</th>
     *     </tr>
     *     <tr>
     *         <td>src</th>
     *         <td>DEFAULT</th>
     *         <td>Yes</th>
     *     </tr>
     *     <tr>
     *         <td>overlay</th>
     *         <td>HOVERED</th>
     *         <td>Yes</th>
     *     </tr>
     *     <tr>
     *         <td>font</td>
     *         <td>CLICKED</td>
     *         <td>No</td>
     *     </tr>
     * </table>
     *
     * @see #getKeysExact(Object, VEffectState)
     */
    public Set<String> getKeysStacked(T part, @Nullable VEffectState state) {
        if (state == null) state = VEffectState.DEFAULT;

        Set<String> buffer = new HashSet<>();

        VEffectState next = state;
        while (next != null) {
            buffer.addAll(getKeysExact(part, next));
            next = next.fallback;
        }

        return buffer;
    }

    public void put(T part, String key, VEffectState state, Object value) {
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

                for (VEffectState targetState : target.getKey(targetPart, targetKey).keySet()) {
                    if (!hasState(targetPart, targetKey, targetState)) {
                        values.get(targetPart).get(targetKey).put(targetState, target.getState(targetPart, targetKey, targetState));
                        continue;
                    }
                }
            }
        }
    }
}
