package net.snackbag.vera.style.animation;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.Events;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

/**
 * Per-widget handler for animations. This is after stylesheet.getKey, so there is no differentiation between
 * widget-specific styles, class styles and standard styles.
 */
public class AnimationEngine {
    public final VWidget<?> widget;
    private final HashMap<VAnimation, Long> activeAnimations = new HashMap<>();
    private final HashMap<VAnimation, UnwindContext> unwindingAnimations = new HashMap<>();
    private final HashMap<VAnimation, RewindContext> rewindingAnimations = new HashMap<>();

    private long cacheId = 0;
    private final HashMap<String, Object> cache = new HashMap<>();

    public AnimationEngine(VWidget<?> widget) {
        this.widget = widget;
    }

    public boolean isActive(String name) {
        return activeAnimations.keySet().stream().anyMatch(anim -> anim.name.equals(name));
    }

    public boolean isUnwinding(String name) {
        return unwindingAnimations.keySet().stream().anyMatch(anim -> anim.name.equals(name));
    }

    public boolean isRewinding(String name) {
        return rewindingAnimations.keySet().stream().anyMatch(anim -> anim.name.equals(name));
    }

    /**
     * Unwinds animations whenever they come to their end.
     * Called in {@link net.snackbag.mcvera.impl.MCVeraRenderer#renderApp(VeraApp)}
     */
    public void update() {
        final long time = System.currentTimeMillis();

        HashMap<VAnimation, Long> animations = (HashMap<VAnimation, Long>) activeAnimations.clone();
        for (VAnimation animation : animations.keySet()) {
            if (time - getTimeSinceActive(animation) >= animation.getTotalTime() - animation.unwindTime) {
                unwind(animation);
            }

            if (time - getTimeSinceActive(animation) >= animation.getTotalTime() && animation.loopMode == LoopMode.NONE)
                kill(animation);
        }
    }

    public void activate(VAnimation animation) {
        activate(animation, false);
    }

    public void activate(VAnimation animation, boolean override) {
        if (!override && isActive(animation.name)) return;
        activeAnimations.put(animation, System.currentTimeMillis());
        widget.events.fire(Events.Animation.BEGIN, animation);
    }

    public void activateOrRewind(VAnimation animation) {
        if (isUnwinding(animation.name)) rewind(animation);
        else activate(animation);
    }

    public void kill(VAnimation animation) {
        Long begin = activeAnimations.remove(animation);
        UnwindContext unwindCtx = unwindingAnimations.remove(animation);
        RewindContext rewindCtx = rewindingAnimations.remove(animation);

        Long unwindBegun = unwindCtx != null ? unwindCtx.begun : null;
        Long rewindBegun = rewindCtx != null ? rewindCtx.begun : null;

        Long nullableBegun = Vera.firstOf(Objects::nonNull, begin, unwindBegun, rewindBegun);

        if (nullableBegun != null) widget.events.fire(Events.Animation.FINISH, animation, nullableBegun);
    }

    public void unwind(VAnimation animation) {
        unwind(animation, false);
    }

    public void unwind(VAnimation animation, boolean override) {
        if (!isActive(animation.name)) return;
        if (!override && isUnwinding(animation.name)) return;

        long time = System.currentTimeMillis();
        widget.events.fire(Events.Animation.UNWIND_BEGIN, animation);

        int rewindProgress = 0;
        if (rewindingAnimations.containsKey(animation)) {
            RewindContext rewindCtx = rewindingAnimations.remove(animation);
            int previousUnwindProgress = rewindCtx.unwindProgress();
            int currentRewindTime = (int) (time - rewindCtx.begun());

            rewindProgress = Math.max(0, previousUnwindProgress - currentRewindTime);
        }

        unwindingAnimations.put(animation, new UnwindContext(time, rewindProgress));
    }

    public void rewind(VAnimation animation) {
        rewind(animation, false);
    }

    public void rewind(VAnimation animation, boolean override) {
        if (!isActive(animation.name)) return;
        if (!unwindingAnimations.containsKey(animation)) return;
        if (!override && isRewinding(animation.name)) return;

        long time = System.currentTimeMillis();
        widget.events.fire(Events.Animation.REWIND_BEGIN, animation);

        UnwindContext unwindCtx = unwindingAnimations.remove(animation);
        int totalUnwindProgress = unwindCtx.rewindProgress() + (int) (time - unwindCtx.begun());

        rewindingAnimations.put(animation, new RewindContext(time, totalUnwindProgress));
    }

    public @Nullable VAnimation getIfEverActive(String name) {
        return activeAnimations.keySet()
                .stream()
                .filter(anim -> anim.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public VAnimation[] getAllActive() {
        return activeAnimations.keySet().toArray(new VAnimation[0]);
    }

    public VAnimation[] getAllUnwinding() {
        return unwindingAnimations.keySet().toArray(new VAnimation[0]);
    }

    public VAnimation[] getAllRewinding() {
        return rewindingAnimations.keySet().toArray(new VAnimation[0]);
    }

    public void checkCache() {
        if (cacheId != Vera.renderCacheId) {
            cache.clear();
            cacheId = Vera.renderCacheId;
        }
    }

    public <T> T animateStyle(String style, T value) {
        checkCache();

        if (value == null) return null;
        if (!cache.containsKey(style)) {
            StyleValueType type = StyleValueType.get(value, null);
            cache.put(style, widget.app.pipeline.applyComposites(this, style, type, value));
        }

        return (T) cache.get(style);
    }

    /**
     * Gets the time an animation has been active since, if it isn't active at all it will return -1
     *
     * @param animation the animation to check
     * @return when the animation was started
     */
    public long getTimeSinceActive(VAnimation animation) {
        return activeAnimations.getOrDefault(animation, -1L);
    }

    public @Nullable UnwindContext getUnwindContext(VAnimation animation) {
        return unwindingAnimations.getOrDefault(animation, null);
    }

    public @Nullable RewindContext getRewindContext(VAnimation animation) {
        return rewindingAnimations.getOrDefault(animation, null);
    }

    /**
     * Context for animation rewinding
     *
     * @param begun the timestamp when the animation started rewinding
     * @param unwindProgress the amount of milliseconds the animation has already been unwinding
     */
    public record RewindContext(Long begun, int unwindProgress) {}

    /**
     * Context for animation unwinding
     *
     * @param begun the timestamp when the animation started unwinding
     * @param rewindProgress the amount of milliseconds the animation has already been rewinding
     */
    public record UnwindContext(Long begun, int rewindProgress) {}
}
