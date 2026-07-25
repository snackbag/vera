package net.snackbag.vera.style.animation;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleValue;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.easing.VEasings;
import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.util.VDataHelper;
import net.snackbag.vera.widget.VWidget;

import java.util.*;
import java.util.function.Consumer;

public class VAnimation {
    public static final String INTERNAL_TRANSITION_NAME = "internal-vera-transition";
    public String name;
    public VLoopMode loopMode;
    public List<VKeyframe> keyframes;

    public VAnimation(String name, VLoopMode loopMode, List<VKeyframe> keyframes) {
        this.name = name.toLowerCase();
        this.loopMode = loopMode;
        this.keyframes = keyframes;
    }

    public CompiledAnimation compile(VeraApp app, VWidget<?> widget) {
        VKeyframe.ListIndex index = VKeyframe.createIndex(keyframes, app.styleSheet);

        List<VKeyframe> extendedFrames = new ArrayList<>();
        List<VKeyframe> loopingFrames = new ArrayList<>(keyframes);
        HashMap<String, StyleValue> styleMemory = new HashMap<>();
        HashMap<String, StyleValue> styleDefaults = new HashMap<>();
        HashSet<String> explicitlySet = new HashSet<>();

        loopingFrames.add(0, new VKeyframe(0, 0, VEasings.IMMEDIATE));

        for (VKeyframe original : loopingFrames) {
            VKeyframe frame = new VKeyframe(original); // copy keyframe
            HashMap<String, StyleValue> frameStyles = index.keyframeValueMap.get(original); // get all registered styles

            // populate all styles & memorize
            for (String style : index.styles) {
                if (frame.styles.containsKey(style)) {
                    // skip & remove keys that weren't reserved when the index was created; fixes NPE
                    if (frameStyles.get(style) == null || app.styleSheet.getKey(widget, style) == null) {
                        frame.styles.remove(style);
                        continue;
                    }

                    styleMemory.put(style, frameStyles.get(style));
                    explicitlySet.add(style);
                    continue;
                }

                // set default style
                if (!styleDefaults.containsKey(style)) {
                    StyleValue sv = new StyleValue(
                            app.styleSheet.getReservation(style),
                            app.styleSheet.getKey(widget, style));
                    styleMemory.put(style, sv);
                    styleDefaults.put(style, sv);
                }

                // actual populating
                StyleValue fallback = explicitlySet.contains(style)
                        ? styleMemory.get(style)
                        : styleDefaults.get(style);
                frame.style(style, fallback.value());
            }

            extendedFrames.add(frame);
        }

        if (loopMode == VLoopMode.FORWARD_REPEAT) { // add immediate end for smooth transition
            VKeyframe frame = new VKeyframe(
                    keyframes.get(keyframes.size() - 1),
                    0,
                    0,
                    VEasings.IMMEDIATE
            );
            extendedFrames.add(frame);
        }

        // convert SVTs
        for (VKeyframe frame : extendedFrames) {
            HashMap<String, Object> fixedStyles = new HashMap<>();

            for (Map.Entry<String, Object> entry : frame.styles.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                value = VDataHelper.unwrapSingleElementArray(value);
                value = StyleValueType.convert(value, app.styleSheet.getReservation(key));

                fixedStyles.put(key, value);
            }

            frame.styles.putAll(fixedStyles);
        }

        return new CompiledAnimation(
                app,
                name, index.totalDuration,
                loopMode,
                extendedFrames, index.styles
        );
    }

    @Override
    public String toString() {
        return "VAnimation{name='%s'}".formatted(name);
    }

    public static class Builder {
        private final String name;
        private final List<VKeyframe> keyframes = new ArrayList<>();

        private VLoopMode loopMode = VLoopMode.NONE;

        public Builder(String name) {
            this.name = name;
        }

        public Builder keyframe(int stayMs, Consumer<VKeyframe> apply) {
            return keyframe(0, stayMs, apply, VEasings.getDefault());
        }

        public Builder keyframe(int transitionMs, int stayMs, Consumer<VKeyframe> apply) {
            return keyframe(transitionMs, stayMs, apply, VEasings.getDefault());
        }

        public Builder keyframe(int transitionMs, int stayMs, Consumer<VKeyframe> apply, VEasing easing) {
            VKeyframe frame = new VKeyframe(stayMs, transitionMs, easing);
            apply.accept(frame);

            keyframes.add(frame);
            return this;
        }

        public Builder loopMode(VLoopMode mode) {
            this.loopMode = mode;
            return this;
        }

        public VAnimation build() {
            return new VAnimation(name, loopMode, keyframes);
        }
    }
}
