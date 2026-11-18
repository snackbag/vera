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
    public final VLoopMode loopMode;

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
            VLoopMode loopMode,
            List<VKeyframe> keyframes, List<String> keys
    ) {
        this.app = app;

        this.name = name;
        this.duration = duration;
        this.loopMode = loopMode;
        this.keyframes = keyframes;
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "CompiledAnimation{name='%s', duration='%s', loopMode='%s', keyframeCount=%s}"
                .formatted(name, duration, loopMode, keyframes.size());
    }

    public String toBeautifulString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Animation: ").append(name).append('\n');
        sb.append("Duration : ").append(duration).append(" ms\n");
        sb.append("LoopMode : ").append(loopMode).append('\n');
        sb.append("Keyframes: ").append(keyframes.size()).append("\n\n");

        for (int i = 0; i < keyframes.size(); i++) {
            VKeyframe frame = keyframes.get(i);
            int start = getWhenKeyframe(frame);

            sb.append('[').append(i).append("] @").append(start).append(" ms\n");
            sb.append("├─ Transition : ").append(frame.transitionTime).append(" ms\n");
            sb.append("├─ Stay       : ").append(frame.stayTime).append(" ms\n");
            sb.append("├─ Easing     : ").append(frame.easing).append('\n');
            sb.append("└─ Styles\n");

            int styleCount = frame.styles.size();
            int index = 0;

            for (var entry : frame.styles.entrySet()) {
                boolean last = ++index == styleCount;

                sb.append("   ")
                        .append(last ? "└─ " : "├─ ")
                        .append(entry.getKey())
                        .append(" = ")
                        .append(entry.getValue())
                        .append('\n');
            }

            if (styleCount == 0) sb.append("   └─ <none>\n");
            if (i + 1 < keyframes.size()) sb.append('\n');
        }

        return sb.toString();
    }

    public int getKeyframeIndexAtTime(int time) {
        int bufferTime = 0;

        for (int i = 0; i < keyframes.size(); i++) {
            VKeyframe keyframe = keyframes.get(i);

            if (keyframe.transitionTime <= 0 && keyframe.stayTime <= 0) continue;
            if (time > bufferTime && time < bufferTime + keyframe.transitionTime + keyframe.stayTime) {
                return i;
            }

            bufferTime += keyframe.transitionTime + keyframe.stayTime;
        }

        return keyframes.size() - 1;
    }

    public int getWhenKeyframe(VKeyframe keyframe) {
        int buffer = 0;

        for (VKeyframe frame : keyframes) {
            if (frame == keyframe) break;
            buffer += frame.transitionTime + frame.stayTime;
        }

        return buffer;
    }

    public float getKeyframeDelta(int time, VKeyframe from, VKeyframe to) {
        return getKeyframeDelta(time, getWhenKeyframe(from), from, to);
    }

    public float getKeyframeDelta(int time, int whenFrom, VKeyframe from, VKeyframe to) {
        int fromTime = whenFrom + from.transitionTime + from.stayTime;
        int toTime = fromTime + to.transitionTime;

        if (time > toTime) return 1f;
        else if (time > fromTime) return (float) (time - fromTime) / (toTime - fromTime); // time <= toTime already true

        // time < fromTime
        return 0f;
    }
}
