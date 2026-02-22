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

    public void startOrRewind(VAnimation animation) {
        if (active.containsKey(animation.name)) active.get(animation.name).rewind();
        else start(animation);
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

    public void unwind(VAnimation animation) {
        unwind(animation.name);
    }

    public void unwind(String name) {
        if (active.containsKey(name)) active.get(name).unwind();
        else MinecraftVera.LOGGER.warn("Couldn't unwind %s, because it's not active".formatted(name));
    }

    public <T> T animateStyle(String key, T value) {
        for (PlaybackContext ctx : active.values()) {
            CompiledAnimation animation = ctx.animation;
            if (!animation.keys.contains(key)) continue;

            int time = ctx.getRelativeTime();

            // select active keyframes
            int kfIndex = animation.getKeyframeIndexAtTime(time);
            int fromKfIndex = Math.max(kfIndex - 1, 0);
            boolean loopSpoofed = false;

            if (ctx.getCurrentLoopNumber() > 1) { // handle loop mode; spoof first keyframe with last one for smooth transition
                if (fromKfIndex == 0) {
                    fromKfIndex = animation.keyframes.size() - 1;
                    loopSpoofed = true;
                }
            }

            VKeyframe to = animation.keyframes.get(kfIndex);
            VKeyframe from = animation.keyframes.get(fromKfIndex);

            int fromKfWhen = animation.getWhenKeyframe(from);
            if (ctx.getCurrentLoopNumber() > 1 && loopSpoofed) { // handle loop mode; spoof beginning time for smooth transition
                fromKfWhen = 0;
            }

            float delta = animation.getKeyframeDelta(time, fromKfWhen, from, to);

            StyleValueType reservation = widget.app.styleSheet.getReservation(key);
            T kfEase = (T) reservation.animationTransition.apply( // ease keyframe transition
                    from.styles.get(key), to.styles.get(key),
                    to.easing, delta);
            T windingEase = (T) reservation.animationTransition.apply( // ease winding
                    kfEase, widget.app.styleSheet.getKey(widget, key),
                    animation.unwindEasing, ctx.getWindingProgress()
            );
            return windingEase;
        }

        return value;
    }

    public void updateLifetimes() {
        for (Map.Entry<String, PlaybackContext> entry : active.entrySet()) {
            PlaybackContext ctx = entry.getValue();
            String name = entry.getKey();
            CompiledAnimation animation = ctx.animation;

            if (animation.loopMode == VLoopMode.NONE && ctx.getProgress() >= 1.0f) {
            ctx.potentiallyResetWinding();
                unwind(name);
            }

            if (ctx.getWindingProgress() >= 1.0f) stop(name);
        }
    }
}
