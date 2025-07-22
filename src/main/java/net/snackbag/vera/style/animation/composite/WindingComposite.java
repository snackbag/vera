package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;

public class WindingComposite extends Composite {
    private long time;

    @Override
    public void generateUniforms() {
        time = System.currentTimeMillis();
    }

    @Override
    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        AnimationEngine engine = ctx.engine();

        T out = in;

        for (VAnimation animation : engine.getAllUnwinding()) {
            if (!animation.affects(ctx.style())) continue;
            if (animation.unwindTime <= 0) {
                out = ctx.original();
                continue;
            }

            long sinceUnwinding = engine.getTimeSinceUnwinding(animation);
            int relativeTime = (int) (time - sinceUnwinding);
            float delta = Math.min((float) relativeTime / (float) animation.unwindTime, 1f);

            out = (T) ctx.type().animationTransition.apply(in, ctx.original(), animation.unwindEasing, delta);
        }

        for (VAnimation animation : engine.getAllRewinding()) {
            if (!animation.affects(ctx.style())) continue;
            if (animation.unwindTime <= 0) continue;

            AnimationEngine.RewindContext rewindCtx = engine.getRewindContext(animation);

            long sinceRewinding = rewindCtx.since();
            int unwindProgress = animation.unwindTime - rewindCtx.unwindProgress();
            int relativeTime = (int) (time - sinceRewinding);

            float delta = (unwindProgress + relativeTime) / (float) animation.unwindTime;

            out = (T) ctx.type().animationTransition.apply(ctx.original(), in, animation.unwindEasing, delta);
        }

        return out;
    }
}
