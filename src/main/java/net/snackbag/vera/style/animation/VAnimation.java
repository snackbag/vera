package net.snackbag.vera.style.animation;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.animation.easing.Easings;
import net.snackbag.vera.style.animation.easing.VEasing;

import java.util.ArrayList;
import java.util.List;
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

    public VAnimation(String name, int unwindTime, VEasing unwindEasing, LoopMode loopMode, VeraApp app) {
        this.name = name;
        this.unwindTime = unwindTime;
        this.unwindEasing = unwindEasing;
        this.loopMode = loopMode;

        this.app = app;
    }

    public void addKeyframe(VKeyframe keyframe) {
        this.keyframes.add(keyframe);
    }

    public static class Builder {
        private final String name;
        private final VeraApp app;

        private LoopMode loopMode = LoopMode.NONE;
        private int unwindTime = 0;
        private VEasing unwindEasing = Easings.LINEAR;

        private final List<VKeyframe> keyframes = new ArrayList<>();

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
            VKeyframe target = new VKeyframe(transitionMs, stayMs);
            frame.accept(target);

            keyframes.add(target);
            return this;
        }

        public VAnimation build() {
            VAnimation animation = new VAnimation(name, unwindTime, unwindEasing, loopMode, app);

            for (VKeyframe frame : keyframes) {
                animation.addKeyframe(frame);
            }

            return animation;
        }
    }
}
