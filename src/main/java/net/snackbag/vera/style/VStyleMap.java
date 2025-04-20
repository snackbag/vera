package net.snackbag.vera.style;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.modifier.VStyleable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class VStyleMap {
    private final HashMap<String, VStyleable> styles = new HashMap<>();

    public VStyleMap() {}

    public void put(String field, VStyleable s) {
        if (!styles.containsKey(field)) {
            styles.put(field, s);
            return;
        }

        final VStyleable at = styles.get(field);
        if (!at.getStylingTypeId().equals(s.getStylingTypeId()))
            throw new UnsupportedOperationException("(Vera) Cannot set style field '" + field + "' to styleable type '" + s.getStylingTypeId() + "', because is already reserved for '" + at.getStylingTypeId() + "'");

        styles.put(field, s);
    }

    public @Nullable VColor getColor(String field) {
        if (!styles.containsKey(field)) return null;
        return styles.get(field) instanceof VColor res ? res : null;
    }

    public @Nullable <T> T get(String field) {
        if (!styles.containsKey(field)) return null;
        return (T) styles.get(field);
    }

    public void merge(VStyleMap map) {
        merge(map, false);
    }

    public void merge(VStyleMap map, boolean force) {
        for (String key : map.styles.keySet()) {
            if (force) styles.put(key, map.styles.get(key));
            else put(key, map.styles.get(key));
        }
    }
}
