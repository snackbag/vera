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


            AnimationEngine.UnwindContext unwindCtx = engine.getUnwindContext(animation);
            int totalProgress = unwindCtx.rewindProgress() + (int) (time - unwindCtx.begun());
            float delta = Math.min((float) totalProgress / (float) animation.unwindTime, 1f);

            out = (T) ctx.type().animationTransition.apply(in, ctx.original(), animation.unwindEasing, delta);
        }

        for (VAnimation animation : engine.getAllRewinding()) {
            if (!animation.affects(ctx.style())) continue;
            if (animation.unwindTime <= 0) {
                out = in;
                continue;
            }

            AnimationEngine.RewindContext rewindCtx = engine.getRewindContext(animation);
            int currentRewindTime = (int) (time - rewindCtx.begun());

            // calculate reverse: start from how much we had unwound, go back towards 0
            int remainingUnwindProgress = Math.max(0, rewindCtx.unwindProgress() - currentRewindTime);
            float delta = Math.min((float) remainingUnwindProgress / (float) animation.unwindTime, 1f);

            out = (T) ctx.type().animationTransition.apply(in, ctx.original(), animation.unwindEasing, delta);
        }

        return out;
    }
}
