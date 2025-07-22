package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;

public class AnimationComposite extends Composite {
    private final HashMap<AnimationEngine, VAnimation[]> animations = new HashMap<>();
    private final HashMap<VAnimation, Long> animationTimes = new HashMap<>();
    private long time; // so we don't have to call it again

    @Override
    public void generateUniforms() {
        animations.clear();
        animationTimes.clear();
        time = System.currentTimeMillis();
    }

    @Override
    public void applyWidget(VWidget<?> widget) {
        VAnimation[] active = widget.animations.getAllActive();

        // Only store if we have active animations
        if (active.length > 0) {
            animations.put(widget.animations, active);

            for (VAnimation animation : active) {
                animationTimes.put(animation, time - widget.animations.getTimeSinceActive(animation));
            }
        }
    }

    @Override
    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        // Most caching isn't necessary, since it's done earlier.

        AnimationEngine engine = ctx.engine();
        VAnimation[] engineAnimations = animations.get(engine);

        // early return
        if (engineAnimations == null || engineAnimations.length == 0) return in;

        T out = in;
        for (VAnimation animation : engineAnimations) {
            // No need to check affects, since calculateStyle does this internally
            T rv = animation.calculateStyle(
                    ctx.style(),
                    ctx.original(),
                    ctx.type(),
                    animationTimes.get(animation),
                    true
            );

            if (rv != null) out = rv;
        }

        return out;
    }
}
