package net.snackbag.vera.style.animation;

import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.style.StyleValue;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.animation.easing.VEasing;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class VKeyframe {
    protected final int stayTime;
    protected final int transitionTime;
    protected final VEasing easing;

    protected final HashMap<String, Object> styles = new HashMap<>();

    public VKeyframe(int stayTime, int transitionTime, VEasing easing) {
        this.stayTime = stayTime;
        this.transitionTime = transitionTime;
        this.easing = easing;
    }

    public VKeyframe(VKeyframe other) {
        this(other, other.transitionTime, other.stayTime, other.easing);
    }

    public VKeyframe(VKeyframe other, int transitionTime, int stayTime, VEasing easing) {
        this.transitionTime = transitionTime;
        this.stayTime = stayTime;
        this.easing = easing;

        styles.putAll(other.styles);
    }

    public void style(String key, Object value) {
        if (styles.containsKey(key)) throw new RuntimeException("Key '%s' cannot be reassigned within the same keyframe".formatted(key));
        styles.put(key, value);
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("transitionTime=").append(transitionTime).append('\n');
        sb.append("stayTime=").append(stayTime).append('\n');
        sb.append("easing=").append(easing).append('\n');
        for (var e : styles.entrySet()) {
            sb.append(e.getKey()).append(" = ").append(e.getValue()).append('\n');
        }
        return sb.toString();
    }

    public static class ListIndex {
        public final List<String> styles = new ArrayList<>();
        public final LinkedHashMap<VKeyframe, HashMap<String, StyleValue>> keyframeValueMap = new LinkedHashMap<>();
        public final int totalDuration;

        /**
         * Gathers styles and total duration of a given list of keyframes.
         *
         * @param keyframes the list of keyframes
         * @param sheet the stylesheet to check keyframe style registration. If null, check will be omitted
         */
        private ListIndex(List<VKeyframe> keyframes, @Nullable VStyleSheet sheet) {
            int dur = 0;

            // counting
            for (VKeyframe frame : keyframes) {
                HashMap<String, StyleValue> valueMap = new HashMap<>();
                dur += frame.transitionTime + frame.stayTime;

                for (Map.Entry<String, Object> entry : frame.styles.entrySet()) {
                    String key = entry.getKey();
                    Object val = entry.getValue();

                    if (!styles.contains(key)) styles.add(key);

                    SheetCheck: if (sheet != null) {
                        StyleValueType res = sheet.getReservation(key);
                        if (res == null) {
                            MinecraftVera.LOGGER.warn("Cannot set keyframe style to unreserved style key: %s. Key removed.".formatted(key));
                            styles.remove(key);
                            frame.styles.remove(key);
                            break SheetCheck;
                        }

                        StyleValueType testRes = StyleValueType.get(val, res);
                        if (testRes != res) throw new RuntimeException(
                                "Cannot create keyframe with key '%s', because it has an incorrect value type. Got: %s, require %s"
                                .formatted(key, testRes, res));

                        valueMap.put(key, new StyleValue(res, val));
                    }

                    keyframeValueMap.put(frame, valueMap);
                }
            }

            // apply values
            totalDuration = dur;
        }
    }

    public static ListIndex createIndex(List<VKeyframe> keyframes, @Nullable VStyleSheet sheet) {
        return new ListIndex(keyframes, sheet);
    }
}
