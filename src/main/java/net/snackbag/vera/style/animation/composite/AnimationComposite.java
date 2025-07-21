package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;

public class AnimationComposite extends Composite {
    private final HashMap<AnimationEngine, VAnimation[]> animations = new HashMap<>();

    @Override
    public void generateUniforms() {
        animations.clear();
    }

    @Override
    public void applyWidget(VWidget<?> widget) {
        animations.put(widget.animations, widget.animations.getAllActive());
    }

    @Override
    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        // No caching necessary, since this is done earlier.

        AnimationEngine engine = ctx.engine();

        T out = in;
        for (VAnimation animation : animations.get(engine)) {
            // No need to check affects, since calculateStyle does this internally
            T rv = animation.calculateStyle(ctx.style(), ctx.original(), ctx.type(), System.currentTimeMillis() - engine.getTimeSinceActive(animation));
            if (rv != null) out = rv;
        }

        return out;
    }
}
