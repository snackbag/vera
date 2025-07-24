package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VAnimation;
import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;

public class WindingComposite extends Composite {
    private final HashMap<AnimationEngine, VAnimation[]> unwindingAnimations = new HashMap<>();
    private final HashMap<AnimationEngine, VAnimation[]> rewindingAnimations = new HashMap<>();
    private long time;

    @Override
    public void generateUniforms() {
        time = System.currentTimeMillis();
        unwindingAnimations.clear();
        rewindingAnimations.clear();
    }

    @Override
    public void applyWidget(VWidget<?> widget) {
        unwindingAnimations.put(widget.animations, widget.animations.getAllUnwinding());
        rewindingAnimations.put(widget.animations, widget.animations.getAllRewinding());
    }

    @Override
    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        AnimationEngine engine = ctx.engine();
        VAnimation[] unwinding = unwindingAnimations.get(engine);
        VAnimation[] rewinding = rewindingAnimations.get(engine);

        T out = in;
        T original = ctx.original();
        String style = ctx.style();

        for (VAnimation animation : unwinding) {
            if (!animation.affects(style)) continue;
            if (animation.unwindTime <= 0) {
                out = original;
                continue;
            }

            AnimationEngine.UnwindContext unwindCtx = engine.getUnwindContext(animation);
            int totalProgress = unwindCtx.rewindProgress() + (int) (time - unwindCtx.begun());
            float delta = Math.min((float) totalProgress / (float) animation.unwindTime, 1f);

            out = (T) ctx.type().animationTransition.apply(in, original, animation.unwindEasing, delta);
        }

        for (VAnimation animation : rewinding) {
            if (!animation.affects(style)) continue;
            if (animation.unwindTime <= 0) {
                out = in;
                continue;
            }

            AnimationEngine.RewindContext rewindCtx = engine.getRewindContext(animation);
            int currentRewindTime = (int) (time - rewindCtx.begun());

            // calculate reverse: start from how much we had unwound, go back towards 0
            int remainingUnwindProgress = Math.max(0, rewindCtx.unwindProgress() - currentRewindTime);
            float delta = Math.min((float) remainingUnwindProgress / (float) animation.unwindTime, 1f);

            out = (T) ctx.type().animationTransition.apply(in, original, animation.unwindEasing, delta);
        }

        return out;
    }
}
