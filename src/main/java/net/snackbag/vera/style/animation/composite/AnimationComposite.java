package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;

public class AnimationComposite extends Composite {
    private final HashMap<AnimationEngine, VAnimation[]> animations = new HashMap<>();
    private final HashMap<VAnimation, Long> animationTimes = new HashMap<>();

    @Override
    public void generateUniforms() {
        animations.clear();
        animationTimes.clear();
    }

    @Override
    public void applyWidget(VWidget<?> widget) {
        VAnimation[] active = widget.animations.getAllActive();
        animations.put(widget.animations, active);

        for (VAnimation animation : active) {
            animationTimes.put(animation, System.currentTimeMillis() - widget.animations.getTimeSinceActive(animation));
        }
    }

    @Override
    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        // Most caching isn't necessary, since it's done earlier.

        AnimationEngine engine = ctx.engine();

        T out = in;
        for (VAnimation animation : animations.get(engine)) {
            // No need to check affects, since calculateStyle does this internally
            T rv = animation.calculateStyle(
                    ctx.style(),
                    ctx.original(),
                    ctx.type(),
                    animationTimes.get(animation)
            );

            if (rv != null) out = rv;
        }

        return out;
    }
}
