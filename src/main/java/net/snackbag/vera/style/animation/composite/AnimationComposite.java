package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;

import java.util.HashMap;

public class AnimationComposite extends Composite {
    private final HashMap<AnimationEngine, VAnimation[]> animations = new HashMap<>();
    private final HashMap<VAnimation, HashMap<String, Object>> precalculatedStyles = new HashMap<>();

    @Override
    public <T> T apply(Context<T> ctx, boolean isNewFrame) {
        AnimationEngine engine = ctx.engine();

        if (isNewFrame) {
            animations.put(engine, engine.getAllActive());
            precalculatedStyles.clear();
        }

        T out = ctx.in();
        VAnimation[] localAnimations = animations.get(engine);

        // If no animations active, return the input
        if (localAnimations == null) return out;

        String style = ctx.style();

        for (VAnimation animation : localAnimations) {
            if (!animation.affects(style)) continue;

            boolean animationCached = precalculatedStyles.containsKey(animation);

            // If animation isn't cached or the key isn't cached
            if (!animationCached || !precalculatedStyles.get(animation).containsKey(style)) {
                if (!animationCached) precalculatedStyles.put(animation, new HashMap<>());

                precalculatedStyles.get(animation).put(
                        style,
                        animation.calculateStyle(
                                style,
                                System.currentTimeMillis() - engine.getTimeSinceActive(animation)
                        )
                );
            }

            out = (T) precalculatedStyles.get(animation).get(style);
        }

        return out;
    }
}
