package net.snackbag.vera.style.animation;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleValue;
import net.snackbag.vera.style.animation.easing.VEasings;
import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.widget.VWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class VAnimation {
    public String name;
    public VLoopMode loopMode;
    public List<VKeyframe> keyframes;

    public VEasing unwindEasing;
    public int unwindTime;
    public boolean unwindAtEnd;

    public VAnimation(String name, VLoopMode loopMode, VEasing unwindEasing, int unwindTime, boolean unwindAtEnd, List<VKeyframe> keyframes) {
        this.name = name.toLowerCase();
        this.loopMode = loopMode;
        this.keyframes = keyframes;
        this.unwindEasing = unwindEasing;
        this.unwindTime = unwindTime;
        this.unwindAtEnd = unwindAtEnd;
    }

    public CompiledAnimation compile(VeraApp app, VWidget<?> widget) {
        VKeyframe.ListIndex index = VKeyframe.createIndex(keyframes, app.styleSheet);

        List<VKeyframe> extendedFrames = new ArrayList<>();
        List<VKeyframe> loopingFrames = new ArrayList<>(keyframes);
        HashMap<String, StyleValue> styleMemory = new HashMap<>();

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
                    continue;
                }

                // set default style
                if (!styleMemory.containsKey(style)) {
                    styleMemory.put(style, new StyleValue(
                                    app.styleSheet.getReservation(style),
                                    app.styleSheet.getKey(widget, style))
                    );
                }
                frame.style(style, styleMemory.get(style).value()); // actual populating
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

        return new CompiledAnimation(
                app,
                name, index.totalDuration,
                loopMode,
                unwindEasing, unwindTime, unwindAtEnd,
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
        private VEasing unwindEasing = VEasings.LINEAR;
        private int unwindTime = 0;
        private boolean unwindAtEnd = false;

        public Builder(String name) {
            this.name = name;
        }

        public Builder keyframe(int stayMs, Consumer<VKeyframe> apply) {
            return keyframe(0, stayMs, apply, VEasings.LINEAR);
        }

        public Builder keyframe(int transitionMs, int stayMs, Consumer<VKeyframe> apply) {
            return keyframe(transitionMs, stayMs, apply, VEasings.LINEAR);
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

        public Builder unwindEasing(VEasing easing) {
            this.unwindEasing = easing;
            return this;
        }

        public Builder unwindTime(int ms) {
            this.unwindTime = ms;
            return this;
        }

        public Builder relativeUnwindTime() {
            this.unwindTime = -1;
            return this;
        }

        public Builder unwindAtEnd() {
            this.unwindAtEnd = true;
            return this;
        }

        public VAnimation build() {
            return new VAnimation(name, loopMode, unwindEasing, unwindTime, unwindAtEnd, keyframes);
        }
    }
}
