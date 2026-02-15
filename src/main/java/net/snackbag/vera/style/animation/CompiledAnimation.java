package net.snackbag.vera.style.animation;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.animation.easing.VEasing;
import net.snackbag.vera.widget.VWidget;

import java.util.List;

/**
 * Version of the animation class that is immutable and has additional values: {@link #app}, {@link #duration} and
 * {@link #keys}. The {@link #keyframes} variable has also been altered drastically.
 */
public class CompiledAnimation {
    /**
     * Assigned app, used for stylesheets
     */
    public final VeraApp app;

    public final String name;
    public final VEasing unwindEasing;
    public final int unwindTime;

    /**
     * Sum of all keyframes's transition time + stay time
     */
    public final int duration;
    /**
     * List of all keyframes. The difference to the normal {@link VAnimation} object is that each keyframe has the
     * style keys of each keyframe in the entire animation, even if unchanged.
     */
    public final List<VKeyframe> keyframes;
    /**
     * All keys that are affected by the animation. Used in caching to calculate animation styles.
     */
    public final List<String> keys;

    /**
     * Does not compile anything by itself, it simply accepts precompiled values. Compilation occurs in
     * {@link VAnimation#compile(VeraApp, VWidget)}
     */
    protected CompiledAnimation(
            VeraApp app,
            String name, int duration,
            VEasing unwindEasing, int unwindTime,
            List<VKeyframe> keyframes, List<String> keys
    ) {
        this.app = app;

        this.name = name;
        this.duration = duration;
        this.unwindEasing = unwindEasing;
        this.unwindTime = unwindTime;
        this.keyframes = keyframes;
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "CompiledAnimation{name='%s', duration='%s', unwindEasing='%s', unwindTime=%s, keyframeCount=%s}"
                .formatted(name, duration, unwindEasing, unwindTime, keyframes.size());
    }
}
