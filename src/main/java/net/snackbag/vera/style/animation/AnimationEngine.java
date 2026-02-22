package net.snackbag.vera.style.animation;

import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;
import java.util.Map;

public class AnimationEngine {
    public final VWidget<?> widget;
    private final HashMap<String, PlaybackContext> active = new HashMap<>();

    public AnimationEngine(VWidget<?> widget) {
        this.widget = widget;
    }

    public void start(VAnimation animation) {
        if (active.containsKey(animation.name)) {
            MinecraftVera.LOGGER.warn("Couldn't start animation %s, because it's already running".formatted(animation.name));
            return;
        }

        CompiledAnimation compiled = animation.compile(widget.app, widget);
        active.put(compiled.name, new PlaybackContext(compiled, System.currentTimeMillis()));
    }

    public void stop(VAnimation animation) {
        stop(animation.name);
    }

    public void stop(String name) {
        if (!active.containsKey(name)) {
            MinecraftVera.LOGGER.warn("Couldn't stop animation %s, because it isn't active".formatted(name));
            return;
        }

        active.remove(name);
    }

    public <T> T animateStyle(String key, T value) {
        for (PlaybackContext ctx : active.values()) {
            CompiledAnimation animation = ctx.animation;
            if (!animation.keys.contains(key)) continue;

            int time = ctx.getRelativeTime();

            // select active keyframe
            int kfIndex = animation.getKeyframeIndexAtTime(time);

            VKeyframe to = animation.keyframes.get(kfIndex);
            VKeyframe from = animation.keyframes.get(Math.max(kfIndex - 1, 0));

            float delta = animation.getKeyframeDelta(time, from, to);

            StyleValueType reservation = widget.app.styleSheet.getReservation(key);
            return (T) reservation.animationTransition.apply(
                    from.styles.get(key), to.styles.get(key),
                    to.easing, delta);
        }

        return value;
    }

    public void updateLifetimes() {
        for (Map.Entry<String, PlaybackContext> entry : active.entrySet()) {
            PlaybackContext ctx = entry.getValue();
            CompiledAnimation animation = ctx.animation;

            if (animation.loopMode == VLoopMode.NONE && ctx.getProgress() >= 1.0f) {
                // TODO: unwinding
                stop(entry.getKey());
            }
        }
    }
}
