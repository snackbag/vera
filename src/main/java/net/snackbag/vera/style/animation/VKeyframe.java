package net.snackbag.vera.style.animation;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.easing.Easings;
import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.util.Once;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.HashMap;

public class VKeyframe {
    protected final Once<VAnimation> animation = new Once<>();
    protected final int transitionTime;
    protected final int stayTime;
    protected final int cumulatedTime;
    protected final HashMap<String, Pair<StyleValueType, Object>> styles = new HashMap<>();

    public @NotNull VEasing easeIn = Easings.LINEAR;

    public VKeyframe(int transitionTime, int stayTime) {
        this.transitionTime = transitionTime;
        this.stayTime = stayTime;
        this.cumulatedTime = this.transitionTime + this.stayTime;
    }

    public void style(String key, Object value) {
        StyleValueType reservation = animation.get().app.styleSheet.getReservation(key);
        if (reservation == null) throw new UnsupportedOperationException("Cannot set keyframe style to unreserved style key");

        animation.get().styleAffections.merge(key, 1, Integer::sum);

        Object converted = StyleValueType.convert(value, reservation);
        styles.put(key, new Pair<>(reservation, converted));
    }

    public boolean affects(String style) {
        return styles.containsKey(style);
    }
}
