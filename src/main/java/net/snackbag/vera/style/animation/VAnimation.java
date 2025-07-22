package net.snackbag.vera.style.animation;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.easing.Easings;
import net.snackbag.vera.style.animation.easing.VEasing;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents an animation template scoped to a specific {@link VeraApp} instance.
 * <p/>
 * Animations are app-specific and should not be shared across multiple apps.
 * If you need to reuse an animation in different apps, define a method that
 * generates a new instance of the animation for each target app.
 */
public class VAnimation {
    public final String name;
    public final VeraApp app;

    public final int unwindTime;
    public final VEasing unwindEasing;
    public final LoopMode loopMode;

    private final List<VKeyframe> keyframes = new ArrayList<>();
    protected final HashMap<String, Integer> styleAffections = new HashMap<>();

    private int totalTime = 0;

    public VAnimation(String name, int unwindTime, VEasing unwindEasing, LoopMode loopMode, VeraApp app) {
        this.name = name;
        this.unwindTime = unwindTime;
        this.unwindEasing = unwindEasing;
        this.loopMode = loopMode;

        this.app = app;
    }

    public void addKeyframe(VKeyframe keyframe) {
        this.keyframes.add(keyframe);
        keyframe.animation.set(this);
        totalTime += keyframe.cumulatedTime;
    }

    public boolean affects(String style) {
        return styleAffections.containsKey(style);
    }

    public <T> @Nullable T calculateStyle(String style, T original, StyleValueType svt, long ms) {
        // TODO: implement loop modes

        if (!affects(style)) {
            return null;
        }

        int margin = 0;
        for (int i = 0; i < keyframes.size(); i++) {
            VKeyframe frame = keyframes.get(i);

            if (ms >= margin && ms <= margin + frame.cumulatedTime) {
                int timeInFrame = (int) ms - margin;
                boolean isTransition = timeInFrame <= frame.transitionTime;

                if (!isTransition) return (T) frame.styles.get(style).getB();

                T before;
                T after = getStyleForKeyframeDeep(i, style);

                if (i > 0) before = getStyleForKeyframeDeep(i - 1, style);
                else before = original;

                float progress = timeInFrame / (float) frame.transitionTime;

                return (T) svt.animationTransition.apply(before, after, frame.easeIn, progress);
            }

            margin += frame.cumulatedTime;
        }

        return original;
    }

    public <T> T getStyleForKeyframeDeep(int targetIndex, String style) {
        VKeyframe target = keyframes.get(targetIndex);

        for (int i = targetIndex; !target.styles.containsKey(style); i--) {
            target = keyframes.get(i - 1);
        }

        Pair<StyleValueType, Object> pair = target.styles.get(style);
        return (T) StyleValueType.convert(pair.getB(), pair.getA());
    }

    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VAnimation animation)) return false;
        return Objects.equals(name, animation.name) && Objects.equals(app, animation.app);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, app);
    }

    public VKeyframe[] getKeyframes() {
        return keyframes.toArray(new VKeyframe[0]);
    }

    public static class Builder {
        private final String name;
        private final VeraApp app;

        private LoopMode loopMode = LoopMode.NONE;
        private int unwindTime = 0;
        private VEasing unwindEasing = Easings.LINEAR;

        private final List<Pair<VKeyframe, Consumer<VKeyframe>>> keyframes = new ArrayList<>();

        public Builder(VeraApp app, String name) {
            this.name = name;
            this.app = app;
        }

        public Builder loop(LoopMode mode) {
            this.loopMode = mode;
            return this;
        }

        public Builder unwindTime(int ms) {
            this.unwindTime = ms;
            return this;
        }

        public Builder unwindEasing(VEasing easing) {
            this.unwindEasing = easing;
            return this;
        }

        public Builder keyframe(int transitionMs, Consumer<VKeyframe> frame, int stayMs) {
            keyframes.add(new Pair<>(new VKeyframe(transitionMs, stayMs), frame));
            return this;
        }

        public VAnimation build() {
            VAnimation animation = new VAnimation(name, unwindTime, unwindEasing, loopMode, app);

            for (Pair<VKeyframe, Consumer<VKeyframe>> frame : keyframes) {
                animation.addKeyframe(frame.getA());
                frame.getB().accept(frame.getA());
            }

            return animation;
        }
    }
}
