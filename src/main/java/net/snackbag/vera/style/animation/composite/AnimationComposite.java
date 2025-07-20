package net.snackbag.vera.style.animation.composite;

public class AnimationComposite extends Composite {
    @Override
    public <T> T apply(Context<T> ctx, boolean isNewFrame) {
        return ctx.in();
    }
}
