package net.snackbag.vera.style.animation;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.util.Once;
import oshi.util.tuples.Pair;

import java.util.HashMap;

public class VKeyframe {
    protected final Once<VAnimation> animation = new Once<>();
    protected final int transitionTime;
    protected final int stayTime;
    protected final HashMap<String, Pair<StyleValueType, Object>> styles = new HashMap<>();

    public VKeyframe(int transitionTime, int stayTime) {
        this.transitionTime = transitionTime;
        this.stayTime = stayTime;
    }

    public void style(String key, Object value) {
        StyleValueType reservation = animation.get().app.styleSheet.getReservation(key);
        if (reservation == null) throw new UnsupportedOperationException("Cannot set keyframe style to unreserved style key");

        Object converted = StyleValueType.convert(value, reservation);
        styles.put(key, new Pair<>(reservation, converted));
    }
}
